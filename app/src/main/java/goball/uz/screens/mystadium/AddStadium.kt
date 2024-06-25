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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import goball.uz.R
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        var textStadiumName by remember { mutableStateOf("") }
        var textDescription by remember { mutableStateOf("") }
        var textStadiumCost by remember { mutableStateOf("") }
        var textStadiumPhoneNumber by remember { mutableStateOf("") }
        var textVerticalLength by remember { mutableStateOf("") }
        var textHorizontalLength by remember { mutableStateOf("") }
        var selectedImageUris by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }
        var selectedLatitute by remember {
            mutableStateOf(0.0)
        }
        var selectedLongitute by remember {
            mutableStateOf(0.0)
        }
        var stadiumLocationIsPicked by remember {
            mutableStateOf(false)
        }

        var pickedStartTime by remember {
            mutableStateOf(LocalTime.NOON)
        }
        var pickedEndTime by remember {
            mutableStateOf(LocalTime.NOON)
        }

        val workingDuration by remember {
            derivedStateOf {
                Duration.between(pickedStartTime, pickedEndTime).let {
                    val hours = it.toHours()
                    val minutes = it.toMinutes() % 60
                    String.format("%d soat va  %d minut", hours, minutes)
                }
            }
        }
        var timeStartAlreadyPicked by remember {
            mutableStateOf(false)
        }
        var timeEndAlreadyPicked by remember {
            mutableStateOf(false)
        }
        val timeDialogStateStart = rememberMaterialDialogState()
        val timeDialogStateEnd = rememberMaterialDialogState()
        val options = listOf("Ommaviy", "Cheklangan")
        var selectedOptionText by remember { mutableStateOf(options[0]) }
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
                Spacer(modifier = Modifier.size(7.dp))
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
                Spacer(modifier = Modifier.size(7.dp))
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
            item {
                Spacer(modifier = Modifier.size(7.dp))
            }
            item {
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1.0F)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 7.dp, end = 5.dp)
                            .border(
                                width = 1.3.dp,
                                color = colorResource(id = R.color.gray),
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        TextField(
                            value = textVerticalLength,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.vertical_length),
                                    contentDescription = null
                                )
                            },
                            maxLines = 1,
                            placeholder = {
                                Text(
                                    "Uzunligi metr",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                            },
                            onValueChange = { textVerticalLength = it },
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
                    Column(
                        modifier = Modifier
                            .weight(1.0F)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 7.dp, start = 5.dp)
                            .border(
                                width = 1.3.dp,
                                color = colorResource(id = R.color.gray),
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        TextField(
                            value = textHorizontalLength,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.horizontal_length),
                                    contentDescription = null
                                )
                            },
                            maxLines = 1,
                            placeholder = {
                                Text(
                                    "Eni metr",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                            },
                            onValueChange = { textHorizontalLength = it },
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
            item {
                Spacer(modifier = Modifier.size(14.dp))
            }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Column(
                            modifier = Modifier
                                .weight(1.0F)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { timeDialogStateStart.show() }
                                .border(
                                    width = 1.3.dp,
                                    color = colorResource(id = R.color.gray),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clock_icon),
                                    contentDescription = "Clock icon",
                                )
                                if (!timeStartAlreadyPicked) {
                                    Text(
                                        text = "Ochilish vaqti",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(
                                            vertical = 18.dp,
                                            horizontal = 5.dp
                                        ),
                                        color = Color.Gray
                                    )
                                } else {
                                    Text(
                                        text = "$pickedStartTime dan",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(
                                            vertical = 18.dp,
                                            horizontal = 5.dp
                                        ),
                                        color = Color.Black
                                    )
                                }
                            }


                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Column(
                            modifier = Modifier
                                .weight(1.0F)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { timeDialogStateEnd.show() }
                                .border(
                                    width = 1.3.dp,
                                    color = colorResource(id = R.color.gray),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (!timeEndAlreadyPicked) {
                                Text(
                                    text = "Yopilish vaqti",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 18.dp),
                                    color = Color.Gray
                                )
                            } else {
                                Text(
                                    text = "$pickedEndTime gacha",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 18.dp),
                                    color = Color.Black
                                )
                            }

                        }

                    }
                    if (timeStartAlreadyPicked && timeEndAlreadyPicked) {
                        Text(
                            text = "Ish vaqti $pickedStartTime dan boshlab $pickedEndTime gacha ($workingDuration)",
                            color = colorResource(
                                id = R.color.primary
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }
            item {
                Spacer(modifier = Modifier.size(14.dp))
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
                        value = textStadiumPhoneNumber,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.phone_icon),
                                contentDescription = null
                            )
                        },
                        maxLines = 1,
                        placeholder = {
                            Text(
                                "Telefon raqam",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        },
                        onValueChange = { textStadiumPhoneNumber = it },
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
                            keyboardType = KeyboardType.Phone
                        )
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(14.dp))
            }
            item {
                SpinnerSample(
                    list = options,
                    preselected = options.first(),
                    onSelectedChanged = { item -> selectedOptionText = item },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.size(14.dp))
            }
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
                            navigator?.push(LocationSelectionScreen(onLocationSelected = { lat, lon ->
                                Toast
                                    .makeText(context, "$lat && $lon", Toast.LENGTH_SHORT)
                                    .show()
                                selectedLatitute = lat
                                selectedLongitute = lon
                                stadiumLocationIsPicked = true
                            }))
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
                            model = R.drawable.location_icon,
                            contentDescription = "Upload location"
                        )
                    }
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(
                        text = "Stadion manzili",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(
                            id = R.color.primary
                        )
                    )
                    Spacer(modifier = Modifier.size(7.dp))
                    Text(
                        text = "Xaritadan kiritish",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                if (!stadiumLocationIsPicked) {
                    Text(
                        text = "Lat: $selectedLatitute && Lon: $selectedLongitute",
                        color = colorResource(
                            id = R.color.primary
                        ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(14.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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
                        value = textDescription,
                        placeholder = {
                            Text(
                                "Qo'shimcha ma'lumot",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        },
                        onValueChange = { textDescription = it },
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize(),
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            containerColor = Color.Transparent,
                            cursorColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                }
            }
        }
        MaterialDialog(
            dialogState = timeDialogStateStart,
            buttons = {
                positiveButton(
                    text = "Tanlash",
                    textStyle = MaterialTheme.typography.bodySmall
                ) {
                    timeStartAlreadyPicked = true
                }
                negativeButton(
                    text = "Bekor qilish",
                    textStyle = MaterialTheme.typography.bodySmall
                ) {
                    timeStartAlreadyPicked = false
                }
            },
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Custom Title
                Text(
                    text = "Yopilish vaqtini kiriting",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,  // Customize this color
                    modifier = Modifier.padding(16.dp)
                )

                // TimePicker
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "",
                    colors = com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults.colors(
                        activeBackgroundColor = colorResource(id = R.color.primary_light),
                        headerTextColor = Color.Black,
                        inactiveBackgroundColor = colorResource(id = R.color.gray),
                        activeTextColor = Color.White,
                        borderColor = Color.Gray,
                        selectorColor = colorResource(id = R.color.primary),
                        inactivePeriodBackground = colorResource(id = R.color.gray),
                        inactiveTextColor = Color.Black,
                        selectorTextColor = Color.White
                    ),
                    is24HourClock = true,
                ) {
                    pickedStartTime = it
                }
            }
        }
        MaterialDialog(
            dialogState = timeDialogStateEnd,
            buttons = {
                positiveButton(
                    text = "Tanlash",
                    textStyle = MaterialTheme.typography.bodySmall
                ) {
                    timeEndAlreadyPicked = true
                }
                negativeButton(
                    text = "Bekor qilish",
                    textStyle = MaterialTheme.typography.bodySmall
                ) {
                    timeEndAlreadyPicked = false
                }
            },
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Custom Title
                Text(
                    text = "Yopilish vaqtini kiriting",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,  // Customize this color
                    modifier = Modifier.padding(16.dp)
                )

                // TimePicker
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "",
                    colors = com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults.colors(
                        activeBackgroundColor = colorResource(id = R.color.primary_light),
                        headerTextColor = Color.Black,
                        inactiveBackgroundColor = colorResource(id = R.color.gray),
                        activeTextColor = Color.White,
                        borderColor = Color.Gray,
                        selectorColor = colorResource(id = R.color.primary),
                        inactivePeriodBackground = colorResource(id = R.color.gray),
                        inactiveTextColor = Color.Black,
                        selectorTextColor = Color.White
                    ),
                    is24HourClock = true,
                ) {
                    pickedEndTime = it
                }
            }
        }
        // TODO: After selecting time we should show the user the duration of the working hours as a warning 
    }

    @Composable
    private fun SpinnerSample(
        list: List<String>,
        preselected: String,
        onSelectedChanged: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var selected by remember { mutableStateOf(preselected) }
        var expanded by remember { mutableStateOf(false) } // initial value
        Box(modifier = modifier) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = selected,
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!expanded) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                modifier = Modifier
                    .wrapContentSize(align = Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.End),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectedChanged(selected)
                        }
                    )
                }
            }
        }
    }
}