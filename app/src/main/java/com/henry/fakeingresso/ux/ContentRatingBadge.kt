package com.henry.fakeingresso.ux

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henry.fakeingresso.ui.theme.Rating10Blue
import com.henry.fakeingresso.ui.theme.Rating12Yellow
import com.henry.fakeingresso.ui.theme.Rating14Orange
import com.henry.fakeingresso.ui.theme.Rating16Red
import com.henry.fakeingresso.ui.theme.Rating18Black
import com.henry.fakeingresso.ui.theme.RatingFreeGreen
import com.henry.fakeingresso.ui.theme.TextDarkGray
import com.henry.fakeingresso.ui.theme.TextWhite

@Composable
fun ContentRatingBadge(rating: String, modifier: Modifier = Modifier) {
    val badgeColor = when {
        rating == "L" || rating == "Livre" -> RatingFreeGreen
        rating.contains("10") -> Rating10Blue
        rating.contains("12") -> Rating12Yellow
        rating.contains("14") -> Rating14Orange
        rating.contains("16") -> Rating16Red
        rating.contains("18") -> Rating18Black
        else -> TextDarkGray
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(badgeColor)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rating,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
