package goball.uz.screens.mystadium

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
        val fusedLocationProviderClient =
            remember { LocationServices.getFusedLocationProviderClient(context) }

        // Permissions request launcher
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                getUserLocation(fusedLocationProviderClient, context, mapView)
            } else {
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Request location permissions
        val requestLocationPermission = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getUserLocation(fusedLocationProviderClient, context, mapView)
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        // Initialize Yandex MapKit
        DisposableEffect(Unit) {
            MapKitFactory.initialize(context)
            mapView = MapView(context)
            mapView?.let {
                it.mapWindow.map.move(
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
                            onClick = { requestLocationPermission() },
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
                                    contentDescription = "Current Location Icon",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                    }

                }

                Button(
                    onClick = {
                        mapView?.let {
                            val centerPoint = it.mapWindow.map.cameraPosition.target
                            onLocationSelected(centerPoint.latitude, centerPoint.longitude)
                            navigator?.pop()
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

    @SuppressLint("MissingPermission")
    private fun getUserLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        context: Context,
        mapView: MapView?
    ) {
        if ((context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            ) ||
            (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocationPoint = Point(location.latitude, location.longitude)
                    mapView?.mapWindow?.map?.move(
                        CameraPosition(userLocationPoint, 15.0f, 0.0f, 0.0f),
                        com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 1f),
                        null
                    )
                } else {
                    Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Manzilni olib bo'lmadi", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Iltimos joylashuvingizni yoqing!", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
}
