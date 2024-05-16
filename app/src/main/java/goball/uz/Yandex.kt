package goball.uz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class Yandex : AppCompatActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("42c1c9b7-5b9f-4fc3-92f0-efcc45ec8dd6")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_yandex)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mapView = findViewById(R.id.mapview)
        mapView.mapWindow.map.move(
            CameraPosition(
                Point(41.2995, 69.2401),
                13.0f,
                0.0f,
                30.0f
            )
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}