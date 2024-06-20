package goball.uz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.helper.FinishActivityState
import goball.uz.screens.AboutUsScreen
import goball.uz.screens.HelpScreen
import goball.uz.screens.MainScreen
import goball.uz.screens.mystadium.MyStadiums
import goball.uz.screens.SettingsScreen
import goball.uz.ui.theme.GoBallTheme

@AndroidEntryPoint
open class ComposeActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItemID = intent.getIntExtra("menu_item", 0)

        setContent {
            GoBallTheme {
                if (FinishActivityState.shouldFinish.value) {
                    finishWithAnimation()
                    FinishActivityState.shouldFinish.value=false
                }

                when (menuItemID) {
                    1 -> Navigator(screen = MyStadiums ())
                    2 -> Navigator(screen = SettingsScreen())
                    3 -> Navigator(screen = AboutUsScreen())
                    4 -> Navigator(screen = HelpScreen())
                }
            }
        }
    }

    private fun finishWithAnimation() {
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}

@Composable
fun ComposableScreen(menuItem: Int?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    ) {
        Text(text = "Composable")
    }
}