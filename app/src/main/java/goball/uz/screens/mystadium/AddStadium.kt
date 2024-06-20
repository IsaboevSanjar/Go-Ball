package goball.uz.screens.mystadium

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddingProcess(modifier: Modifier) {
        val context = LocalContext.current
        var textStadiumName by remember { mutableStateOf("") }
        var textStadiumCost by remember { mutableStateOf("") }
        var selectedImageUris by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { uris ->
                if (uris.size > 5) {
                    Toast.makeText(
                        context,
                        "Please select up to 5 images only",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    selectedImageUris = uris
                }
            }
        )

        LazyColumn(modifier.padding(16.dp)) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.3.dp,
                            color = colorResource(id = R.color.gray),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            imagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .padding(14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .size(45.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                color = colorResource(id = R.color.gray),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = R.drawable.upload_cloud,
                            contentDescription = "Upload Images"
                        )
                    }
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(
                        text = "Rasm yuklash",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(
                            id = R.color.primary
                        )
                    )
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(
                        text = "SVG, PNG, JPG (max. 800x400px)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(7.dp))
            }
            item {
                if (selectedImageUris.isNotEmpty()) {
                    LazyRow {
                        items(selectedImageUris) { uri ->
                            Box(
                                modifier = Modifier
                                    .size(width = 115.dp, height = 107.dp)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))  // Clipping with rounded corners
                                    .background(Color.Gray)
                            )  // Background color to show rounded corners)
                            {

                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                AsyncImage(
                                    model = R.drawable.x_close,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        .clickable {

                                            Toast
                                                .makeText(context, "$uri", Toast.LENGTH_SHORT)
                                                .show()
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.3.dp,
                            color = colorResource(id = R.color.gray),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    TextField(
                        value = textStadiumName,
                        placeholder = {
                            Text(
                                "Stadion nomi",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        },
                        onValueChange = { textStadiumName = it },
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor = Color.Gray
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(top = 7.dp)
                        .border(
                            width = 1.3.dp,
                            color = colorResource(id = R.color.gray),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    TextField(
                        value = textStadiumCost,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.price_gray),
                                contentDescription = null
                            )
                        },
                        maxLines = 1,
                        placeholder = {
                            Text(
                                "Soatlik narxi (so'm)",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        },
                        onValueChange = { textStadiumCost = it },
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }

        }
    }

}