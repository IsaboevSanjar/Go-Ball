package goball.uz.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import goball.uz.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
) {
    var isHalfStar = (rating % 1) != 0.0

    Row {
        for (index in 1..5) {
            Icon(
                imageVector = if (index <= rating) {
                    Icons.Rounded.Star
                }else{
                     if (isHalfStar){
                         Icons.Rounded.StarHalf
                     }
                    else{
                        Icons.Rounded.StarOutline
                     }
                     }, contentDescription = null,
                tint = colorResource(id = R.color.primary),
                modifier = modifier
            )
        }
    }
}