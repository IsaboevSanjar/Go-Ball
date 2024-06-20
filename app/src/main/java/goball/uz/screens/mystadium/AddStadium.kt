package goball.uz.screens.mystadium

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import goball.uz.R

class AddStadium : Screen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Stadion qo'shish",
                        modifier = Modifier.padding(start = 9.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator?.pop()
                        }) {

                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Go back",
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            AddingProcess(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

    }

    @Composable
    fun AddingProcess(modifier: Modifier) {
        val context = LocalContext.current
        var selectedImageUris by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult =  { uris ->
                if (uris.size > 5) {
                    Toast.makeText(
                        context,
                        "Please select up to 5 images only",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    selectedImageUris=uris
                }
            }
        )

        LazyColumn(modifier) {
            item {
                Button(
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "Pick photos")
                }
            }
            items(selectedImageUris) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}