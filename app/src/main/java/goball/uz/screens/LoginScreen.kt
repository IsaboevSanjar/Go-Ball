package goball.uz.screens

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import goball.uz.R

class LoginScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        var otpText by remember {
            mutableStateOf("")
        }
        var loading by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "Kodni kiriting", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(20.dp))
                Row (verticalAlignment = Alignment.CenterVertically){
                    ClickableText(
                        onClick = { offset ->
                            println("You clicked on offset $offset")
                            Log.d("TAGG", "You clicked on offset $offset")
                        },
                        text =buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Green,
                                fontSize = 17.sp,fontWeight = FontWeight.Bold), ) {
                                append("@go_ballbot")
                            }
                        }
                    )
                    Text(
                        text = " telegram botga kiring",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value = otpText,
                    onValueChange = { newText ->
                        // Ensure new text only contains digits (0-9)
                        val filteredText = newText.filter { it.isDigit() }
                        if (filteredText.length <= 4) {
                            otpText = filteredText
                        }
                    },
                    singleLine = true // Prevent line breaks within the text field
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        repeat(4) { index ->
                            val number = otpText.getOrNull(index)?.toString()
                                ?: "" // Use getOrNull for safer indexing

                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = number,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(2.dp)
                                        .background(Color.Black)
                                )
                            }

                        }
                    }
                }
            }
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                onClick = {/* navigator?.push(MainScreen())*/loading = !loading },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 17.dp)
            ) {
                if (!loading) {
                    Text(text = "Tasdiqlash", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text(text = "Loading", style = MaterialTheme.typography.titleMedium)
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
