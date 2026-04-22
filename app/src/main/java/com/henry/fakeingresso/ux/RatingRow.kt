package com.henry.fakeingresso.ux

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.henry.fakeingresso.ui.theme.RatingGold
import com.henry.fakeingresso.ui.theme.TextDarkGray
import com.henry.fakeingresso.ui.theme.TextWhite

@SuppressLint("DefaultLocale")
@Composable
fun RatingRow(rating: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val starValue = (index + 1) * 2.0
            val tint = if (rating >= starValue) RatingGold
            else if (rating >= starValue - 1.0) RatingGold.copy(alpha = 0.5f)
            else TextDarkGray

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%.1f", rating),
            color = TextWhite,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
