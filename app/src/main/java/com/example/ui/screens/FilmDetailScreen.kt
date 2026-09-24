package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.AppDatabase
import com.example.data.local.entity.WatchlistEntity
import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.example.data.network.IndieMovieDto
import com.example.data.repository.IndieFilmRepository
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryLight
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest

/**
 * 'FilmDetail' Composable that opens when a movie in the indie grid is clicked.
 * Displays comprehensive film information with special prominence on:
 * - Description (Film narrative & synopsis)
 * - Release Year
 * - Director (Auteur / Directorial vision)
 */
@Composable
fun FilmDetail(
    movieId: String,
    onBack: () -> Unit = {},
    onPlayMovie: (String) -> Unit = {},
    onNavigateToReviews: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    FilmDetailScreen(
        movieId = movieId,
        onBack = onBack,
        onPlayMovie = onPlayMovie,
        onNavigateToReviews = onNavigateToReviews,
        modifier = modifier
    )
}

/**
 * Direct overload accepting domain [Movie] object.
 */
@Composable
fun FilmDetail(
    film: Movie,
    onBack: () -> Unit = {},
    onPlayMovie: (String) -> Unit = {},
    onNavigateToReviews: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    FilmDetailContent(
        movie = film,
        onBack = onBack,
        onPlayMovie = onPlayMovie,
        onNavigateToReviews = onNavigateToReviews,
        modifier = modifier
    )
}

/**
 * Direct overload accepting [IndieMovieDto] fetched via Retrofit.
 */
@Composable
fun FilmDetail(
    film: IndieMovieDto,
    onBack: () -> Unit = {},
    onPlayMovie: (String) -> Unit = {},
    onNavigateToReviews: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    FilmDetailContent(
        movie = film.toMovie(),
        onBack = onBack,
        onPlayMovie = onPlayMovie,
        onNavigateToReviews = onNavigateToReviews,
        modifier = modifier
    )
}

/**
 * Primary 'FilmDetailScreen' loading movie state via repositories
 * and rendering detailed theatrical presentation.
 */
@Composable
fun FilmDetailScreen(
    movieId: String,
    onBack: () -> Unit,
    onPlayMovie: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    indieRepository: IndieFilmRepository = remember { IndieFilmRepository() }
) {
    val allMovies by MovieRepository.movies.collectAsState()
    var fetchedFilm by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // First attempt to match from domain repository
    val cachedMovie = allMovies.find { it.id == movieId }

    LaunchedEffect(movieId) {
        if (cachedMovie == null) {
            isLoading = true
            val result = indieRepository.getIndieFilmById(movieId)
            if (result.isSuccess) {
                fetchedFilm = result.getOrNull()?.toMovie()
            }
            isLoading = false
        }
    }

    val displayMovie = cachedMovie ?: fetchedFilm ?: allMovies.firstOrNull() ?: Movie(
        id = movieId,
        title = "Independent Feature",
        posterUrl = "",
        rating = 8.5,
        year = 2024,
        language = "Malayalam",
        genres = listOf("Auteur Drama"),
        duration = "2h 00m",
        synopsis = "A cinematic exploration of contemporary life and auteur storytelling.",
        director = "Acclaimed Director"
    )

    if (isLoading && cachedMovie == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Surface),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = CrimsonPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading Film Master Details...",
                    color = OnSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    } else {
        FilmDetailContent(
            movie = displayMovie,
            onBack = onBack,
            onPlayMovie = onPlayMovie,
            onNavigateToReviews = onNavigateToReviews,
            modifier = modifier
        )
    }
}

/**
 * Parametric heart shape constructed using cubic bezier curves.
 */
val HeartShape: Shape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    // Start at bottom tip
    moveTo(width * 0.5f, height * 0.88f)

    // Left lower curve up towards left side
    cubicTo(
        width * 0.12f, height * 0.64f,
        0f, height * 0.40f,
        0f, height * 0.25f
    )

    // Left top lobe curve
    cubicTo(
        0f, height * 0.06f,
        width * 0.18f, 0f,
        width * 0.36f, 0f
    )

    // Left lobe dipping into center cleft
    cubicTo(
        width * 0.44f, 0f,
        width * 0.48f, height * 0.08f,
        width * 0.5f, height * 0.16f
    )

    // Right lobe curving out from center cleft
    cubicTo(
        width * 0.52f, height * 0.08f,
        width * 0.56f, 0f,
        width * 0.64f, 0f
    )

    // Right top lobe curve
    cubicTo(
        width * 0.82f, 0f,
        width, height * 0.06f,
        width, height * 0.25f
    )

    // Right lower curve down to bottom tip
    cubicTo(
        width, height * 0.40f,
        width * 0.88f, height * 0.64f,
        width * 0.5f, height * 0.88f
    )

    close()
}

/**
 * Floating heart-shaped toggle button that instantly adds or removes the film
 * from the local Room 'Watchlist' database without navigating away.
 */
@Composable
fun FloatingHeartWatchlistButton(
    isWatchlisted: Boolean,
    isPressedAnim: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressedAnim) 1.25f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "heart_scale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .size(64.dp)
            .scale(buttonScale)
            .shadow(
                elevation = if (isWatchlisted) 14.dp else 8.dp,
                shape = HeartShape,
                ambientColor = if (isWatchlisted) CrimsonPrimary.copy(alpha = 0.6f) else Color.Black,
                spotColor = if (isWatchlisted) CrimsonPrimary else Color.Black
            )
            .testTag("floating_heart_watchlist_button"),
        shape = HeartShape,
        color = if (isWatchlisted) CrimsonPrimary else SurfaceContainerHigh.copy(alpha = 0.95f),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isWatchlisted) Color(0xFFFF897D) else Outline.copy(alpha = 0.6f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isWatchlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isWatchlisted) "Remove from Watchlist" else "Save to Watchlist",
                tint = if (isWatchlisted) Color.White else OnSurface,
                modifier = Modifier
                    .size(30.dp)
                    .testTag("floating_heart_icon")
            )
        }
    }
}

@Composable
private fun FilmDetailContent(
    movie: Movie,
    onBack: () -> Unit,
    onPlayMovie: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val isWatchlistedInDb by db.watchlistDao().isMovieInWatchlist(movie.id).collectAsState(initial = movie.isWatchlisted)

    var isExpandedSynopsis by remember { mutableStateOf(false) }
    var isHeartPressedAnim by remember { mutableStateOf(false) }
    var showWatchlistToast by remember { mutableStateOf(false) }

    fun toggleRoomWatchlist() {
        coroutineScope.launch(Dispatchers.IO) {
            val watchlistDao = db.watchlistDao()
            val inDb = watchlistDao.isMovieInWatchlistSync(movie.id)
            if (inDb) {
                watchlistDao.deleteByMovieId(movie.id)
            } else {
                watchlistDao.insertFilm(WatchlistEntity.fromMovie(movie))
            }
            MovieRepository.toggleWatchlist(movie.id)
        }
        coroutineScope.launch {
            isHeartPressedAnim = true
            showWatchlistToast = true
            delay(250)
            isHeartPressedAnim = false
            delay(1800)
            showWatchlistToast = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .testTag("film_detail_container")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("film_detail_screen"),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
        // 1. Cinematic Hero Header with Backdrop Poster and Overlays
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(390.dp)
            ) {
                AsyncImage(
                    model = movie.backdropUrl.ifEmpty { movie.posterUrl },
                    contentDescription = "${movie.title} poster backdrop",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Atmospheric Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Surface.copy(alpha = 0.85f),
                                    Surface
                                ),
                                startY = 0f
                            )
                        )
                )

                // Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerLowest.copy(alpha = 0.7f))
                            .testTag("film_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                toggleRoomWatchlist()
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLowest.copy(alpha = 0.7f))
                                .testTag("film_bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isWatchlistedInDb) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isWatchlistedInDb) "Remove from Watchlist" else "Save to Watchlist",
                                tint = if (isWatchlistedInDb) GoldSecondary else OnSurface
                            )
                        }

                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out '${movie.title}' (${movie.year}) directed by ${movie.director} on Cine Nova Indie!"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Film"))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLowest.copy(alpha = 0.7f))
                                .testTag("film_share_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Film",
                                tint = OnSurface
                            )
                        }
                    }
                }

                // Award / Laurel Banner in Hero
                if (!movie.awards.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLowest.copy(alpha = 0.9f))
                            .border(1.dp, GoldSecondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Festival Award",
                                tint = GoldSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = movie.awards.uppercase(),
                                color = GoldSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.6.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. Film Title & Core Metadata (Year, Rating, Duration, Quality)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Genres chips row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    movie.genres.take(3).forEach { genre ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                .testTag("film_genre_$genre")
                        ) {
                            Text(
                                text = genre,
                                color = CyanTertiary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                Text(
                    text = movie.title,
                    color = OnSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 34.sp,
                    modifier = Modifier.testTag("film_title")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Pill Row highlighting Release Year prominently
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Release Year Tag (Explicitly requested by user)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CrimsonPrimary.copy(alpha = 0.15f))
                            .border(1.dp, CrimsonPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                            .testTag("film_release_year")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Release Year",
                                tint = CrimsonPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${movie.year}",
                                color = CrimsonPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.testTag("film_year")
                            )
                        }
                    }

                    // Rating Tag
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("film_rating")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = GoldSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.rating}",
                            color = OnSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Duration
                    Text(
                        text = movie.duration,
                        color = OnSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Video Quality Master badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceContainerLow)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = movie.videoQuality,
                            color = Outline,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary CTA Buttons: Play Film & Watchlist
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onPlayMovie(movie.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("play_film_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonPrimary,
                            contentColor = OnPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Film",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Stream Master",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            toggleRoomWatchlist()
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("watchlist_button"),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isWatchlistedInDb) GoldSecondary else Outline
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isWatchlistedInDb) GoldSecondary.copy(alpha = 0.15f) else Color.Transparent,
                            contentColor = if (isWatchlistedInDb) GoldSecondary else OnSurface
                        )
                    ) {
                        Icon(
                            imageVector = if (isWatchlistedInDb) Icons.Default.Check else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isWatchlistedInDb) GoldSecondary else OnSurface,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isWatchlistedInDb) "In Watchlist" else "+ Watchlist",
                            color = if (isWatchlistedInDb) GoldSecondary else OnSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 3. Prominent Director Section (Explicitly requested by user)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "DIRECTOR & VISION",
                    color = Outline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerHigh)
                        .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                        .testTag("film_director_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Director Avatar / Initial Circle
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(CrimsonPrimary, GoldSecondary)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = movie.director.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "D" },
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Directed by",
                                    color = Outline,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldSecondary.copy(alpha = 0.2f))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "AUTEUR",
                                        color = GoldSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = movie.director,
                                color = OnSurface,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("film_director")
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = movie.directorRole.ifEmpty { "Director • Screenplay" },
                                color = CyanTertiary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.testTag("film_director_name")
                            )
                        }
                    }
                }
            }
        }

        // 4. Description / Synopsis Section (Explicitly requested by user)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "DESCRIPTION & SYNOPSIS",
                    color = Outline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp)
                        .testTag("film_description_card")
                ) {
                    Column {
                        Text(
                            text = movie.synopsis,
                            color = OnSurface,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            maxLines = if (isExpandedSynopsis) Int.MAX_VALUE else 4,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .testTag("film_description")
                                .testTag("film_synopsis")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isExpandedSynopsis) "Show Less ▴" else "Read Full Synopsis ▾",
                            color = GoldSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isExpandedSynopsis = !isExpandedSynopsis }
                                .padding(vertical = 4.dp)
                                .testTag("toggle_synopsis_button")
                        )
                    }
                }

                // Cinephile Review quote if present
                if (!movie.cinephileReview.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerHigh)
                            .border(1.dp, GoldSecondary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = null,
                                    tint = GoldSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CRITICS CONSENSUS",
                                    color = GoldSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${movie.cinephileReview}”",
                                color = OnSurfaceVariant,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // 5. Featured Cast Members
        if (movie.cast.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CAST & PERFORMANCES",
                            color = Outline,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "${movie.cast.size} Featured",
                            color = Outline,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(movie.cast) { member ->
                            CastMemberCard(member = member)
                        }
                    }
                }
            }
        }

        // 6. Specifications & Master Technical Details
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "SPECIFICATIONS & ARCHIVAL SPECS",
                    color = Outline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLowest)
                        .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SpecRow(label = "Release Year", value = "${movie.year}")
                        SpecRow(label = "Director", value = movie.director)
                        SpecRow(label = "Original Language", value = movie.language)
                        SpecRow(label = "Master Quality", value = movie.videoQuality)
                        SpecRow(label = "Audio Spec", value = movie.audioSpec)
                        SpecRow(label = "Download Footprint", value = movie.downloadSize)
                    }
                }
            }
        }

        // 7. Community Reviews & Critiques (Stored in Room DB)
        item {
            val db = remember { AppDatabase.getDatabase(context) }
            val filmReviewsFlow = remember(movie.id) { db.reviewDao().getReviewsForFilm(movie.id) }
            val filmReviews by filmReviewsFlow.collectAsState(initial = emptyList())

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "COMMUNITY REVIEWS & CRITIQUES",
                            color = Outline,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "${filmReviews.size} Stored Locally in Room",
                            color = CyanTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onNavigateToReviews(movie.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CrimsonPrimary,
                            contentColor = OnPrimaryContainer
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("film_detail_write_review_button")
                    ) {
                        Icon(imageVector = Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Critique", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (filmReviews.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerLowest)
                            .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No reviews yet for ${movie.title}",
                                color = OnSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Be the first to share your thoughts on direction & performances.",
                                color = Outline,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filmReviews.forEach { review ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SurfaceContainerLow)
                                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                                    .testTag("film_detail_review_item")
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = review.reviewerName,
                                            color = CyanTertiary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = GoldSecondary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "${review.rating}",
                                                color = GoldSecondary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = review.reviewText,
                                        color = OnSurface,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Floating Watchlist Toast / Feedback pill
    AnimatedVisibility(
        visible = showWatchlistToast,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 20.dp, bottom = 102.dp)
            .testTag("heart_watchlist_feedback")
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceContainerHighest.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, if (isWatchlistedInDb) CrimsonPrimary else OutlineVariant),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isWatchlistedInDb) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isWatchlistedInDb) CrimsonPrimary else OnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (isWatchlistedInDb) "Saved to Room Watchlist" else "Removed from Watchlist",
                    color = OnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Floating Heart-Shaped Toggle Button
    FloatingHeartWatchlistButton(
        isWatchlisted = isWatchlistedInDb,
        isPressedAnim = isHeartPressedAnim,
        onClick = { toggleRoomWatchlist() },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 20.dp, bottom = 28.dp)
    )
}
}

@Composable
private fun CastMemberCard(member: CastMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(82.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(1.dp, OutlineVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.name.take(2).uppercase(),
                color = GoldSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = member.name,
            color = OnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = member.role,
            color = Outline,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Outline,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = OnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
