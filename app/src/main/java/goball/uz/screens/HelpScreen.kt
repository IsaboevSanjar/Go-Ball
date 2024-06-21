package goball.uz.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import goball.uz.R
import goball.uz.helper.FinishActivityState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HelpScreen() : Screen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Yordam",
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
                    }
                )
            }
        ) {
            var pickedStartTime by remember {
                mutableStateOf(LocalTime.NOON)
            }

            val formattedStartTime by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("hh:mm")
                        .format(pickedStartTime)
                }
            }
            var textOpeningTime by remember {
                mutableStateOf(formattedStartTime)
            }
            val timeDialogStateStart = rememberMaterialDialogState()
            val context = LocalContext.current
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { timeDialogStateStart.show() }) {

                }
                Text(text = formattedStartTime)
                Spacer(modifier = Modifier.height(50.dp))
            }
            MaterialDialog(
                dialogState = timeDialogStateStart,
                buttons = {
                    positiveButton(text = "Tanlash") {
                        Toast.makeText(context, "Tanlandi", Toast.LENGTH_SHORT).show()
                    }
                    negativeButton(text = "Bekor qilish")
                }
            ) {
                timepicker(
                    initialTime = LocalTime.NOON,
                    title = "Ochilish vaqtini kiriting",
                ) {
                    pickedStartTime = it
                }
            }

        }

    }
}