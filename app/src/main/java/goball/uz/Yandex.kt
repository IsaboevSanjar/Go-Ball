package goball.uz

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.databinding.ActivityYandexBinding
import goball.uz.databinding.NavHeaderBinding
import goball.uz.models.staium.StadiumListItem
import goball.uz.presentation.StadiumsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Yandex : AppCompatActivity() {
    private lateinit var binding: ActivityYandexBinding
    private lateinit var headerBinding: NavHeaderBinding
    private lateinit var mapView: MapView
    private val stadiumsViewModel: StadiumsViewModel by viewModels()
    private var userLocationPoint: Point? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val lat = 41.2995
    private val long = 69.2401
    private var stadiumsCount = 0

    companion object{
        init {
            MapKitFactory.setApiKey("42c1c9b7-5b9f-4fc3-92f0-efcc45ec8dd6")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        // Make the app content extend into the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Hide the system bars
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        binding = ActivityYandexBinding.inflate(layoutInflater)
        val view = binding.root
        headerBinding = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        setContentView(view)
        setUpToolbar()
        mapView = binding.mapview
        showMap(mapView)
        setOnClickListeners()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        lifecycleScope.launch {
            stadiumsViewModel.stadiums.collectLatest { stadium ->
                if (stadium.isNotEmpty()) {
                    addPlaceMarkToStadiums(stadium)
                    stadiumsCount = stadium.size
                    binding.stadiumCount.text = "$stadiumsCount  Stadionlar mavjud "
                }
            }
        }
        binding.zoomUserCurrentLocation.setOnClickListener {
            requestLocationPermission()
        }
    }

    private fun setOnClickListeners() {
        binding.logout.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }
        headerBinding.closeDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }
        binding.showStadiums.setOnClickListener {
            val bottomSheet = ComposeBottomSheetDialogFragment(stadiumsViewModel)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.oval_burger_menu)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val intent = Intent(this, ComposeActivity::class.java)
            when (menuItem.itemId) {
                R.id.my_stadiums -> showToastAndNavigate(menuItem, intent, 1)
                R.id.settings -> showToastAndNavigate(menuItem, intent, 2)
                R.id.about_us -> showToastAndNavigate(menuItem, intent, 3)
                R.id.help -> showToastAndNavigate(menuItem, intent, 4)
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun showToastAndNavigate(
        menuItem: MenuItem,
        intent: Intent,
        menuItemId: Int
    ) {
        intent.putExtra("menu_item", menuItemId)
        intent.putExtra("menu_text", menuItem.title)
        startActivity(intent)
    }


    private fun addPlaceMarkToStadiums(stadiums: List<StadiumListItem>) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.stadium_marker)
        // Resize the bitmap to a desired size (adjust width and height as needed)
        val desiredWidth = 120// Adjust width in pixels (e.g., 48, 32)
        val desiredHeight = 120 // Adjust height in pixels (e.g., 48, 32)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, desiredWidth, desiredHeight, false)
        val imageProvider = ImageProvider.fromBitmap(scaledBitmap)
        stadiums.forEach { item ->
            val placeMark = mapView.mapWindow.map.mapObjects.addPlacemark(
                Point(
                    item.lat.toDouble(),
                    item.long.toDouble()
                ), imageProvider
            )
            placeMark.addTapListener(StadiumTapListener(this@Yandex, item.id))
        }
    }

    // Taking user permission to use their location in this app
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        } else {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        userLocationPoint = Point(location.latitude, location.longitude)
                        Log.d("YandexLat", "Location obtained: $userLocationPoint")
                        zoomToUserLocation() // Zoom to location once permission is granted
                    } else {
                        Log.d("YandexLat", "Location is null")
                    }
                }.addOnFailureListener {
                    Log.d("YandexLat", "Failed to get location", it)
                }
            } else {
                promptEnableLocation()
            }
        }
    }


    private fun showMap(mapView: MapView) {
        mapView.mapWindow.map.move(
            CameraPosition(
                Point(lat, long),
                13.0f,
                0.0f,
                30.0f
            ),
            Animation(Animation.Type.SMOOTH, 2f), null
        )

    }

    private fun zoomToUserLocation() {
        userLocationPoint?.let { userLocation ->
            mapView.mapWindow.map.move(
                CameraPosition(userLocation, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f), null
            )
            Log.d("YandexLat", "Zooming to user location: $userLocation")
        } ?: run {
            Toast.makeText(
                this,
                "User location not available: $userLocationPoint",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("YandexLat", "User location not available: $userLocationPoint")
        }
    }



    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptEnableLocation() {
        Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
        val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}