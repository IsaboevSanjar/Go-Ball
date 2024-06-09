package goball.uz

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener

class StadiumTapListener(private val context:Context, private val stadiumId:Int):MapObjectTapListener {
    override fun onMapObjectTap(p0: MapObject, point: Point): Boolean {
        Toast.makeText(
            context,
            "Tapped stadium ID: $stadiumId at point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        return true
    }
}