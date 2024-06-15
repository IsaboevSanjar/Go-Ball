package goball.uz.screens.mystadium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import goball.uz.R

class MyStadiums : Screen {
    @Composable
    override fun Content() {
        val hasStadium by remember {
            mutableStateOf(false)
        }
        var loading by remember {
            mutableStateOf(false)
        }
        val navigator = LocalNavigator.current
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            if (!hasStadium){

                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Image(
                        painter = painterResource(id = R.drawable.no_stadium_adding),
                        contentDescription = "Stadium Adding Image"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Stadion mavjud emas", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Stadioningizni qo'shing va dastur \n yordamida nazorat qiling",
                        style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)

                }

            }
            Spacer(modifier = Modifier.height(50.dp))
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                onClick = {
                          loading=!loading
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (!loading) {
                    Text(text = "Stadion qo'shish", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text(text = "Loading", style = MaterialTheme.typography.titleMedium)
                }


            }

        }

    }
}