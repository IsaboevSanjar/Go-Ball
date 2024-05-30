package goball.uz

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationLayer.*
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider

class Yandex : AppCompatActivity() {
    private lateinit var mapView: MapView
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

    val locations = listOf(
        Point(41.2995, 69.2501),
        Point(41.2995, 69.2401),
        Point(41.2900, 69.2401),
        Point(41.2325, 69.2401),
        Point(41.3100, 69.2300),
        Point(41.3150, 69.2350),
        Point(41.3200, 69.2510),
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("42c1c9b7-5b9f-4fc3-92f0-efcc45ec8dd6")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapKitFactory.initialize(this)
        val mapKit = MapKitFactory.getInstance()
        setContentView(R.layout.activity_yandex)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = findViewById(R.id.mapview)
        requestLocationPermission()

        showMap(mapView)
        val userLocation = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocation.isVisible = true
        userLocation.isHeadingEnabled=true
        val iconStyle = IconStyle().setScale(0.2f)
        val customIcon = ImageProvider.fromResource(this, R.drawable.current_location)
        userLocation.setObjectListener(object :UserLocationObjectListener{
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
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.stadium_marker)
        // Resize the bitmap to a desired size (adjust width and height as needed)
        val desiredWidth = 120// Adjust width in pixels (e.g., 48, 32)
        val desiredHeight = 120 // Adjust height in pixels (e.g., 48, 32)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, desiredWidth, desiredHeight, false)
        val imageProvider = ImageProvider.fromBitmap(scaledBitmap)
        locations.forEach { location ->
            val placeMark = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                geometry = location
                setIcon(imageProvider)
            }
            // Optionally add tap listener for each placemark
            placeMark.addTapListener(placeMarkTapListener)
        }


    }

    //Taking user permission to use their location in this app
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
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