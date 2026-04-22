package com.henry.fakeingresso.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.henry.fakeingresso.R
import com.henry.fakeingresso.ui.theme.DarkBackground
import com.henry.fakeingresso.ui.theme.FavoritePink
import com.henry.fakeingresso.ui.theme.TextWhite

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(DarkBackground.copy(alpha = 0.6f))
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
            ),
            tint = if (isFavorite) FavoritePink else TextWhite
        )
    }
}

@Composable
fun FavoriteIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Filled.Favorite,
        contentDescription = stringResource(R.string.favorites),
        tint = FavoritePink,
        modifier = modifier.size(20.dp)
    )
}
