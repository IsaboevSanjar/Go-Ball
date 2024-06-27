package goball.uz.screens.start

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import goball.uz.R
import goball.uz.Yandex
import goball.uz.app.App
import goball.uz.cache.AppCache
import goball.uz.models.UserData
import goball.uz.presentation.StadiumsViewModel
import goball.uz.ui.theme.fontBold
import kotlinx.coroutines.flow.collectLatest

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<StadiumsViewModel>()
        val navigator = LocalNavigator.current
        val context = LocalContext.current
        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        var success by remember { mutableStateOf(true) }
        var loading by remember { mutableStateOf(false) }

        val focusRequester = remember { FocusRequester() }
        var otpText by remember { mutableStateOf("") }
        var addedText by remember { mutableStateOf("") }

        val loginState by viewModel.login.collectAsState()

        if (otpText.length > 4) {
            otpText = ""
        }

        LaunchedEffect(Unit) {
            clipboardManager.getText()?.text?.let {
                if (it.length == 4 && it.isDigitsOnly()) {
                    otpText = it
                    loading = !loading
                }
            }
            focusRequester.requestFocus()
        }

        LaunchedEffect(loginState) {
            loginState?.let {
                // Handle successful login, such as navigating to another screen or showing a success message
                Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                //AppCache.getHelper().firstOpen = false
                val intent = Intent(context, Yandex::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
                loading = false
                success = true
            } ?: run {
                loading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Kodni kiriting", fontFamily = fontBold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClickableText(
                        onClick = { offset ->
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://t.me/mystadium_bot")
                            )
                            context.startActivity(browserIntent)
                            println("You clicked on offset $offset")
                            Log.d("TAGG", "You clicked on offset $offset")
                        },
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = colorResource(id = R.color.primary),
                                    fontSize = 17.sp, fontWeight = FontWeight.Bold
                                ),
                            ) {
                                append("@go_ballbot")
                            }
                        }
                    )
                    Text(
                        text = " telegram botga kiring",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                BasicTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                                if (addedText.isEmpty() && otpText.isNotEmpty()) {
                                    otpText = addedText
                                }
                            }
                            false
                        },
                    value = otpText,
                    onValueChange = { newText ->
                        // Ensure new text only contains digits (0-9)
                        val filteredText = newText.filter { it.isDigit() }
                        if (filteredText.length <= 4 || otpText.isNotEmpty()) {
                            otpText = filteredText
                            addedText = filteredText
                        }
                    },
                    singleLine = true, // Prevent line breaks within the text field
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        repeat(4) { index ->
                            val number = otpText.getOrNull(index)?.toString() ?: ""
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .width(50.dp)
                                        .background(
                                            Color.LightGray,
                                            shape = RoundedCornerShape(7.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = number,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontSize = 20.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
                if (!success) {
                    Text(
                        text = "kod noto'g'ri",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }
            }

            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                onClick = {
                    if (otpText.isNotEmpty()) {
                        viewModel.loginWithTelegram(otpText)
                        loading = true
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (!loading) {
                    Text(text = "Tasdiqlash", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text(text = "Tekshirmoqda", style = MaterialTheme.typography.titleMedium)
                }
            }

            LaunchedEffect(key1 = viewModel.showErrorToastChannelLogin) {
                viewModel.showErrorToastChannelLogin.collectLatest { show ->
                    if (show) {
                        success = false
                        Toast.makeText(
                            App.instance, "Error", Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                    }
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun LoginPage() {
        Content()
    }

}
