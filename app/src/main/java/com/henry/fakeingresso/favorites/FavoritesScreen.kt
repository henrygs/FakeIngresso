package com.henry.fakeingresso.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.henry.fakeingresso.R
import com.henry.fakeingresso.ux.MovieGridCard
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.ui.theme.DarkBackground
import com.henry.fakeingresso.ui.theme.TextGray
import com.henry.fakeingresso.ui.theme.TextWhite

@Composable
fun FavoritesScreen(
    movies: List<MovieDTO>,
    onMovieClick: (MovieDTO) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (movies.isEmpty()) {
            EmptyFavorites()
        } else {
            FavoritesGrid(movies = movies, onMovieClick = onMovieClick)
        }
    }
}

@Composable
private fun EmptyFavorites() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_favorites),
            color = TextGray,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun FavoritesGrid(
    movies: List<MovieDTO>,
    onMovieClick: (MovieDTO) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(R.string.favorites),
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
        }

        items(movies, key = { it.id }) { movie ->
            MovieGridCard(
                movie = movie,
                isFavorite = true,
                onClick = { onMovieClick(movie) }
            )
        }
    }
}
