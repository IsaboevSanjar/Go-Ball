package goball.uz

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import goball.uz.network.Api
import goball.uz.models.staium.StadiumListItem
import goball.uz.ui.elements.RatingBar


@Composable
fun StadiumsList(context: Context, stadiums: List<StadiumListItem>) {
    var rating_1 by remember {
        mutableDoubleStateOf(3.5)
    }
    Text(
        text = "Sizga eng yaqin stadionlar soni 24", textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.fillMaxWidth(),
    )
    LazyColumn(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.onBackground)
            .padding(top = 15.dp)
    ) {
        item {
            Text(
                text = "Sizga eng yaqin stadionlar soni 24", textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "Top stadionlar", textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, bottom = 8.dp),
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                items((stadiums)) { stadium ->
                    val imageState = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(Api.BASE_URL + stadium.image)
                            .size(Size.ORIGINAL).build()
                    ).state
                    Card(
                        modifier = Modifier
                            .height(310.dp)
                            .width(236.dp)
                            .padding(7.dp)
                            .clickable {
                                Toast
                                    .makeText(context, "" + stadium.name, Toast.LENGTH_SHORT)
                                    .show()
                            }, shape = RoundedCornerShape(16.dp)
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (imageState is AsyncImagePainter.State.Error) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            if (imageState is AsyncImagePainter.State.Success) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    painter = imageState.painter,
                                    contentDescription = stadium.name,
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                painter = painterResource(id = R.drawable.transparent_rectangle),
                                contentDescription = "Transparent image",
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Bottom.also { Arrangement.SpaceAround },
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${stadium.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment=Alignment.CenterVertically
                                ) {
                                    RatingBar(modifier = Modifier.size(14.dp), rating = stadium.rating.toString().toDouble())
                                    Text(
                                        text = "${stadium.rating}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White,
                                    )
                                }

                                Row(Modifier.fillMaxWidth()) {
                                    Image(
                                        painter = painterResource(id = R.drawable.price_green),
                                        contentDescription = "Price Icon"
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${stadium.price} sum/soat",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White,
                                    )
                                }
                            }
                        }

                    }

                }
            }
        }
        item {

            Text(
                text = "Eng yaqindagilar", textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 20.dp, bottom = 8.dp),
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                items((1..10).toList()) {

                    Card(
                        modifier = Modifier
                            .height(310.dp)
                            .width(236.dp)
                            .padding(7.dp)
                            .clickable {
                                Toast
                                    .makeText(context, "" + it, Toast.LENGTH_SHORT)
                                    .show()
                            }, shape = RoundedCornerShape(16.dp)
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.stadium2),
                                contentDescription = "Item Pictures",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                painter = painterResource(id = R.drawable.transparent_rectangle),
                                contentDescription = "Transparent image",
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Bottom.also { Arrangement.SpaceAround },
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Stadion $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Text(
                                    text = "5.0",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                                Row(Modifier.fillMaxWidth()) {
                                    Image(
                                        painter = painterResource(id = R.drawable.price_green),
                                        contentDescription = "Price Icon"
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "200 000 sum/soat",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        }

                    }

                }
            }
        }

    }

}

