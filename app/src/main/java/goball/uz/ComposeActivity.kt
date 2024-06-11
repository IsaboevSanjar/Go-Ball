package goball.uz

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import goball.uz.ui.theme.GoBallTheme

@AndroidEntryPoint
class ComposeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItem = intent.getIntExtra("menu_item",0)
        Log.d("ComposeActivityT", "Received menu_item: $menuItem")  // Ensure this log statement is here
        setContent {
            GoBallTheme {
                ComposableScreen(menuItem)
            }
        }
    }
}

@Composable
fun ComposableScreen(menuItem: Int?) {
   Column(modifier = Modifier
       .fillMaxSize()
       .background(color = Color.Blue)) {
       Text(text = "Composable")
   }
}