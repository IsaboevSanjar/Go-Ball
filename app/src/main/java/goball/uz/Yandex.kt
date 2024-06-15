package goball.uz

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
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
    private val placeMarkTapListener = MapObjectTapListener { id, point ->
        Toast.makeText(
            this@Yandex,
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
    }
    private val lat = 41.2995
    private val long = 69.2401
    private var stadiumsCount = 0


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("42c1c9b7-5b9f-4fc3-92f0-efcc45ec8dd6")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYandexBinding.inflate(layoutInflater)
        val view = binding.root
        headerBinding = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        setContentView(view)
        MapKitFactory.initialize(this)
        val mapKit = MapKitFactory.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set up the toolbar with a drawer icon
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.oval_burger_menu)
        navigationItemClick()

        mapView = findViewById(R.id.mapview)
        requestLocationPermission()
        showMap(mapView)
        userLocation(mapKit)

        lifecycleScope.launch {
            stadiumsViewModel.stadiums.collectLatest { stadium ->
                if (stadium.isNotEmpty()) {
                    addPlaceMarkToStadiums(stadium)
                    stadiumsCount = stadium.size
                    binding.stadiumCount.text = "$stadiumsCount  Stadionlar mavjud "
                }
            }
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

    // Handle navigation item clicks
    @SuppressLint("SetTextI18n")
    private fun navigationItemClick() {
        binding.showStadiums.setOnClickListener {
            val bottomSheet = ComposeBottomSheetDialogFragment(stadiumsViewModel)
            Toast.makeText(this, "$stadiumsCount", Toast.LENGTH_SHORT).show()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.logout.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }
        headerBinding.closeDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            val intent = Intent(this, ComposeActivity::class.java)
            when (menuItem.itemId) {
                R.id.my_stadiums -> {
                    Toast.makeText(this, "My Stadium", Toast.LENGTH_SHORT).show()
                    intent.putExtra("menu_item", 1)
                }

                R.id.settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    intent.putExtra("menu_item", 2)
                    /*setContent {
                        val navigator = LocalNavigator.current
                        GoBallTheme {
                            navigator?.push(LoginScreen())
                        }
                    }*/
                }

                R.id.about_us -> {
                    Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show()
                    intent.putExtra("menu_item", 3)
                }

                R.id.help -> {
                    Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
                    intent.putExtra("menu_item", 4)
                }
            }
            startActivity(intent)
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun userLocation(mapKit: MapKit) {
        val userLocation = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocation.isVisible = true
        userLocation.isHeadingEnabled = true
        val iconStyle = IconStyle().setScale(0.2f)
        val customIcon = ImageProvider.fromResource(this, R.drawable.current_location)
        userLocation.setObjectListener(object : UserLocationObjectListener {
            override fun onObjectAdded(view: UserLocationView) {
                view.pin.setIcon(customIcon, iconStyle)
                view.arrow.setIcon(customIcon, iconStyle)
            }

            override fun onObjectRemoved(p0: UserLocationView) {

            }

            override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

            }

        })
        mapKit.resetLocationManagerToDefault()
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

    //Taking user permission to use their location in this app
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
                1
            )
            return
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

    companion object {
        private val POINT = Point(41.2995, 69.2401)
        private val POSITION = CameraPosition(
            POINT,
            13.0f,
            0.0f,
            30.0f
        )

    }
}