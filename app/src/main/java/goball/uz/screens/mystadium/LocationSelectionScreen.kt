package goball.uz.screens.mystadium

import android.graphics.PointF
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import goball.uz.R

class LocationSelectionScreen(private val onLocationSelected: (latitude: Double, longitude: Double) -> Unit) :
    Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        var mapView by remember { mutableStateOf<MapView?>(null) }
        DisposableEffect(Unit) {
            MapKitFactory.initialize(context)
            mapView = MapView(context)

            onDispose {
                mapView?.onStop()
                MapKitFactory.getInstance().onStop()
            }
        }

        // Remember and initialize Yandex MapKit
        DisposableEffect(Unit) {
            MapKitFactory.initialize(context)
            mapView = MapView(context)
            mapView?.let {
                it.map.move(
                    CameraPosition(Point(41.315486, 69.260827), 14.0f, 0.0f, 0.0f),
                    com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 0f),
                    null
                )
            }
            onDispose {
                mapView?.onStop()
                MapKitFactory.getInstance().onStop()
            }
        }
        mapView?.let { map ->
            LaunchedEffect(map) {
                map.onStart()
            }
            map.mapWindow.map.move(
                CameraPosition(Point(41.315486, 69.260827), 14.0f, 0.0f, 0.0f),
                com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 0f),
                null
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    mapView?.let { view ->
                        AndroidView(factory = { view }, modifier = Modifier.fillMaxSize())
                    }
                    // Center icon
                    Image(
                        painter = painterResource(id = R.drawable.stadium_marker),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(70.dp)
                            .padding(bottom = 8.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(color = Color.Transparent)
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            onClick = { navigator?.pop() },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.size(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.arrow_back),
                                    contentDescription = "Back Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Card(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.size(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.current_location),
                                    contentDescription = "Back Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    }

                }

                Button(
                    onClick = {
                        mapView?.let {
                            val centerPoint = it.map.cameraPosition.target
                            onLocationSelected(centerPoint.latitude, centerPoint.longitude)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp, top = 10.dp, start = 10.dp, end = 10.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary)
                    )
                ) {
                    Text(
                        text = "Tanlash",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(11.dp)
                    )
                }
            }
        }

    }
}
