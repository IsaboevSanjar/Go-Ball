package goball.uz.screens.drawers

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import goball.uz.R
import goball.uz.cache.AppCache
import goball.uz.helper.FinishActivityState
import goball.uz.screens.mystadium.AddStadium

class SettingsScreen() : Screen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var textStadiumPhoneNumber by remember { mutableStateOf("+998 ${AppCache.getHelper().phoneNumber}") }
        var textFullName by remember { mutableStateOf(AppCache.getHelper().fullName) }
        val navigator = LocalNavigator.current
        val options = listOf("O'zbek", "English", "Русский")
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Sozlamalar",
                        modifier = Modifier.padding(start = 9.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                    navigationIcon = {
                        IconButton(onClick = {
                            FinishActivityState.shouldFinish.value = true
                        }) {

                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Go back",
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Rounded.Check, contentDescription ="Save changes", tint = Color.Black)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(top = 36.dp, start = 16.dp, end = 16.dp)
                            .border(
                                width = 1.3.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        TextField(
                            value = textFullName,
                            placeholder = {
                                Text(
                                    "Ismingiz",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                            },
                            onValueChange = { textFullName = it },
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




                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                            .border(
                                width = 1.3.dp,
                                color = colorResource(id = R.color.black),
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
                            readOnly = true,
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
                    SpinnerSample(
                        list = options,
                        preselected = options.first(),
                        onSelectedChanged = { item -> selectedOptionText = item },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp),
                        onClick = {

                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.black),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(vertical = 18.dp)
                    ) {
                        Text(text = "Chiqish", style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Akkountni o'chirish",
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


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