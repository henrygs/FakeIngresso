package com.henry.fakeingresso.detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henry.fakeingresso.R
import com.henry.fakeingresso.components.ContentRatingBadge
import com.henry.fakeingresso.components.FavoriteButton
import com.henry.fakeingresso.components.GenreChips
import com.henry.fakeingresso.components.ImageCarousel
import com.henry.fakeingresso.components.RatingRow
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.ui.theme.DarkBackground
import com.henry.fakeingresso.ui.theme.PrimaryBLueLight
import com.henry.fakeingresso.ui.theme.TextGray
import com.henry.fakeingresso.ui.theme.TextWhite

@Composable
fun DetailScreen(
    movie: MovieDTO?,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    if (movie == null) return

    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val carouselHeight = screenHeight / 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            BackdropSection(movie = movie, carouselHeight = carouselHeight)
            ContentSection(movie = movie)
        }

        BackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp)
        )

        FavoriteButton(
            isFavorite = isFavorite,
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = 12.dp)
        )
    }
}

@Composable
private fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(DarkBackground.copy(alpha = 0.6f))
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back),
            tint = TextWhite
        )
    }
}

@Composable
private fun BackdropSection(movie: MovieDTO, carouselHeight: androidx.compose.ui.unit.Dp) {
    val imageUrls = movie.images.map { it.url }.ifEmpty { listOf(movie.imageFeatured) }

    Box(modifier = Modifier.fillMaxWidth()) {
        ImageCarousel(
            imageUrls = imageUrls,
            height = carouselHeight,
            contentDescription = movie.title
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, DarkBackground)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = movie.title,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (movie.originalTitle.isNotBlank() && movie.originalTitle != movie.title) {
                Text(
                    text = movie.originalTitle,
                    color = TextGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ContentSection(movie: MovieDTO) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(movie = movie)

        Spacer(modifier = Modifier.height(12.dp))

        if (movie.genres.isNotEmpty()) {
            GenreChips(genres = movie.genres)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (movie.rating > 0) {
            RatingRow(rating = movie.rating)
            Spacer(modifier = Modifier.height(16.dp))
        }

        TrailerButton(movie = movie)

        Spacer(modifier = Modifier.height(20.dp))

        if (movie.synopsis.isNotBlank()) {
            SynopsisSection(synopsis = movie.synopsis)
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (movie.cast.isNotBlank()) {
            InfoItem(label = stringResource(R.string.cast), value = movie.cast)
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (movie.director.isNotBlank()) {
            InfoItem(label = stringResource(R.string.director), value = movie.director)
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (movie.premiereDate.localDate.isNotBlank()) {
            InfoItem(
                label = stringResource(R.string.premiere),
                value = "${movie.premiereDate.dayAndMonth} (${movie.premiereDate.dayOfWeek})"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun InfoRow(movie: MovieDTO) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (movie.premiereDate.year.isNotBlank()) {
            Text(
                text = movie.premiereDate.year,
                color = TextGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (movie.duration.isNotBlank()) {
            Text(
                text = movie.duration,
                color = TextGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (movie.contentRating.isNotBlank()) {
            ContentRatingBadge(rating = movie.contentRating)
        }
    }
}

@Composable
private fun TrailerButton(movie: MovieDTO) {
    val context = LocalContext.current
    val trailerUrl = movie.trailers.firstOrNull()?.url

    trailerUrl?.let {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBLueLight)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = TextWhite
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.watch_trailer),
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SynopsisSection(synopsis: String) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.synopsis),
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = synopsis,
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (expanded) Int.MAX_VALUE else 4,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 22.sp
        )
        if (synopsis.length > 200) {
            Text(
                text = stringResource(if (expanded) R.string.show_less else R.string.show_more),
                color = PrimaryBLueLight,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = TextGray,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
    }
}
