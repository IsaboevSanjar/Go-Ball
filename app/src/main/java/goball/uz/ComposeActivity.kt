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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.ui.theme.GoBallTheme

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItem = intent.getIntExtra("menu_item", 0)
        Log.d(
            "ComposeActivityT",
            "Received menu_item: $menuItem"
        )  // Ensure this log statement is here
        setContent {
            GoBallTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Text(
                                text = "Mening Stadionlarim",
                                modifier = Modifier.padding(start = 9.dp),
                                style =MaterialTheme.typography.bodyMedium
                            )
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
                    ComposableScreen(menuItem = menuItem)
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