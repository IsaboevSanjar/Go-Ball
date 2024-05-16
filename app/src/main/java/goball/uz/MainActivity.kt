package goball.uz

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.presentation.StadiumsViewModel
import goball.uz.screens.StartScreen
import goball.uz.ui.theme.GoBallTheme
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /* private val viewModel by viewModels<StadiumsViewModel>(factoryProducer = {
         object : ViewModelProvider.Factory {
             override fun <T : ViewModel> create(modelClass: Class<T>): T {
                 return StadiumsViewModel(StadiumsRepositoryImpl(RetrofitInstance.api))
                         as T
             }
         }
     })*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("42c1c9b7-5b9f-4fc3-92f0-efcc45ec8dd6")
        setContent {
            val viewModel = hiltViewModel<StadiumsViewModel>()
            GoBallTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(screen = StartScreen())
                    //StadiumLists(context = this, viewModel = viewModel)
                }
            }
        }

    }
}

@Composable
fun StadiumLists(context: Context, viewModel: StadiumsViewModel) {
    val stadiumList = viewModel.stadiums.collectAsState().value

    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
        viewModel.showErrorToastChannel.collectLatest { show ->
            if (show) {
                Toast.makeText(
                    context, "Error", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    if (stadiumList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        StadiumsList(context = context, stadiums = stadiumList)
    }
}

@Composable
fun MapScreen() {
    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    )
}

