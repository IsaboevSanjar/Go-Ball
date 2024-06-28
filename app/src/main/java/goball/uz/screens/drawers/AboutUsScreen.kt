package goball.uz.screens.drawers

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yandex.maps.mobile.BuildConfig
import goball.uz.R
import goball.uz.helper.FinishActivityState

class AboutUsScreen() : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Biz haqimizda",
                        modifier = Modifier.padding(start = 9.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                },
                    navigationIcon = {
                        IconButton(onClick = { FinishActivityState.shouldFinish.value = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Go back",
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                )
            }
        ) { paddingValue ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val version = BuildConfig.VERSION_NAME
                val lastUpdatedDate = "28.06.2024"
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Version $version", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = lastUpdatedDate, style = MaterialTheme.typography.bodySmall)
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Condition of service use",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(
                            id = R.color.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Privacy policy",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(
                            id = R.color.primary
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.send_message_icon),
                            contentDescription = "Message",
                            Modifier.padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.rate_app),
                            contentDescription = "Rate",
                            Modifier.padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.share_icon_44),
                            contentDescription = "Share",
                            Modifier.padding(4.dp)
                        )
                    }
                }

            }

        }
    }

}