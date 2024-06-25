package goball.uz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.screens.start.StartScreen
import goball.uz.ui.theme.GoBallTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navigator = LocalNavigator.current
            GoBallTheme {
                Navigator(screen = StartScreen())
                

            }
        }

    }
}
