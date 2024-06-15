package goball.uz

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.screens.AboutUsScreen
import goball.uz.screens.HelpScreen
import goball.uz.screens.mystadium.MyStadiums
import goball.uz.screens.SettingsScreen
import goball.uz.ui.theme.GoBallTheme

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItemText = intent.getStringExtra("menu_text")

        val menuItemID = intent.getIntExtra("menu_item", 0)
        Log.d(
            "ComposeActivityT",
            "Received menu_item: $menuItemID"
        )  // Ensure this log statement is here
        setContent {
            GoBallTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            menuItemText?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(start = 9.dp),
                                    style =MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                            navigationIcon = {
                                IconButton(onClick = { finishWithAnimation() }) {
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
                    if (menuItemID==1){
                        Navigator(screen = MyStadiums())
                    }else if (menuItemID==2){
                        Navigator(screen = SettingsScreen())
                    }
                    else if (menuItemID==3){
                        Navigator(screen = AboutUsScreen())
                    }
                    else if (menuItemID==4){
                        Navigator(screen = HelpScreen())
                    }
                }
            }
        }

    }

    private fun finishWithAnimation() {
        finish()
        // Apply animation when the activity finishes
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