package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.network.IndieMovieDto
import com.example.data.repository.MovieRepository
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
 * Primary 'Discover' Composable:
 * Fetches and displays a list of indie films using Retrofit service,
 * incorporating a dedicated movie data model (IndieMovieDto / Movie)
 * and an adaptive Material 3 Grid layout.
 */
@Composable
fun Discover(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: com.example.ui.viewmodel.DiscoverViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchInput by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .testTag("discover_composable_root")
    ) {
        when (val state = uiState) {
            is com.example.ui.viewmodel.DiscoverUiState.Loading -> {
                IndieLoadingGridSkeleton()
            }

            is com.example.ui.viewmodel.DiscoverUiState.Error -> {
                IndieErrorView(
                    errorMessage = state.message,
                    onRetry = { viewModel.loadFilms() }
                )
            }

            is com.example.ui.viewmodel.DiscoverUiState.Success -> {
                val gridColumns = if (state.isThreeColumns) 3 else 2

                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("indie_film_grid"),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header Item (Span Full Width)
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            // Top Actions Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SurfaceContainerHigh)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(CyanTertiary)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "RETROFIT 2.12 • ${state.apiLatencyMs}ms",
                                        color = CyanTertiary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Grid Columns Switcher (2 or 3 columns)
                                    IconButton(
                                        onClick = { viewModel.toggleGridColumns() },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .testTag("grid_toggle_button")
                                    ) {
                                        Icon(
                                            imageVector = if (state.isThreeColumns) Icons.Default.GridView else Icons.Default.ViewModule,
                                            contentDescription = "Toggle Grid Layout",
                                            tint = OnSurface
                                        )
                                    }

                                    // Refresh / Re-fetch from Retrofit
                                    IconButton(
                                        onClick = { viewModel.loadFilms(isRefresh = true) },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .testTag("refresh_indie_button")
                                    ) {
                                        if (state.isRefreshing) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                strokeWidth = 2.dp,
                                                color = CrimsonPrimary
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Fetch Indie Films",
                                                tint = OnSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Discover Indie Cinema",
                                color = OnSurface,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "Curated festival laureates, auteur masterpieces & micro-budget visions",
                                color = Outline,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Prominent Search Bar at the Top (Always Visible)
                            OutlinedTextField(
                                value = searchInput,
                                onValueChange = {
                                    searchInput = it
                                    viewModel.onSearchQueryChanged(it)
                                },
                                placeholder = {
                                    Text(
                                        text = "Search indie films by title or director...",
                                        fontSize = 13.sp,
                                        color = Outline
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = CyanTertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (searchInput.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                searchInput = ""
                                                viewModel.onSearchQueryChanged("")
                                            },
                                            modifier = Modifier
                                                .size(48.dp)
                                                .testTag("clear_search_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Clear search",
                                                tint = Outline,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceContainerHigh,
                                    unfocusedContainerColor = SurfaceContainerLow,
                                    focusedBorderColor = CrimsonPrimary,
                                    unfocusedBorderColor = OutlineVariant.copy(alpha = 0.6f),
                                    focusedTextColor = OnSurface,
                                    unfocusedTextColor = OnSurface,
                                    cursorColor = CrimsonPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("indie_search_bar")
                                    .testTag("indie_search_input")
                                    .testTag("discover_search_bar")
                            )

                            // Active Search Query Banner
                            if (state.searchQuery.isNotBlank()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, bottom = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Filtering: ",
                                            color = Outline,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "\"${state.searchQuery}\"",
                                            color = CyanTertiary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = " • ${state.films.size} found",
                                            color = Outline,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Text(
                                        text = "Clear Filter ✕",
                                        color = CrimsonPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable {
                                                searchInput = ""
                                                viewModel.onSearchQueryChanged("")
                                            }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                            .testTag("clear_filter_chip")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Category Chips Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewModel.categories.forEach { cat ->
                                    val isSelected = state.activeCategory == cat
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isSelected) CrimsonPrimary else SurfaceContainerHigh)
                                            .clickable { viewModel.selectCategory(cat) }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                            .testTag("chip_$cat")
                                    ) {
                                        Text(
                                            text = cat,
                                            color = if (isSelected) OnPrimaryContainer else OnSurfaceVariant,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Results summary & Sort indicator
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${state.totalCount} Indie Titles Ingested",
                                    color = OnSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sort:",
                                        color = Outline,
                                        fontSize = 11.sp
                                    )

                                    val sortLabels = listOf("default" to "Curated", "rating" to "★ Rating", "year" to "Year")
                                    sortLabels.forEach { (sortKey, label) ->
                                        val isSortActive = state.sortOrder == sortKey
                                        Text(
                                            text = label,
                                            color = if (isSortActive) GoldSecondary else Outline,
                                            fontWeight = if (isSortActive) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp,
                                            modifier = Modifier
                                                .clickable { viewModel.setSortOrder(sortKey) }
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                                .testTag("sort_$sortKey")
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Spotlight Banner for Festival Selection (if available and viewing All)
                    if (state.activeCategory == "All Indie" && state.searchQuery.isBlank() && state.films.isNotEmpty()) {
                        val spotlight = state.films.first()
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            IndieSpotlightBanner(
                                film = spotlight,
                                onClick = { onNavigateToDetail(spotlight.id) },
                                onPlay = { onNavigateToPlayer(spotlight.id) }
                            )
                        }
                    }

                    // Film Grid Items
                    if (state.films.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            IndieEmptyView(
                                query = state.searchQuery,
                                onReset = {
                                    searchInput = ""
                                    viewModel.onSearchQueryChanged("")
                                    viewModel.selectCategory("All Indie")
                                }
                            )
                        }
                    } else {
                        items(
                            items = state.films,
                            key = { it.id }
                        ) { film ->
                            IndieFilmGridCard(
                                film = film,
                                isCompact = state.isThreeColumns,
                                onClick = { onNavigateToDetail(film.id) },
                                onPlay = { onNavigateToPlayer(film.id) },
                                onBookmark = { viewModel.toggleWatchlist(film.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Backward and navigation-friendly wrapper matching MainActivity route
 */
@Composable
fun DiscoverScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    viewModel: com.example.ui.viewmodel.DiscoverViewModel = viewModel()
) {
    Discover(
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToPlayer = onNavigateToPlayer,
        viewModel = viewModel
    )
}

/**
 * Individual Indie Movie Card rendered inside the Discover Grid Layout.
 */
@Composable
fun IndieFilmGridCard(
    film: IndieMovieDto,
    isCompact: Boolean,
    onClick: () -> Unit,
    onPlay: () -> Unit,
    onBookmark: () -> Unit
) {
    val movies by MovieRepository.movies.collectAsState()
    val isWatchlisted = movies.find { it.id == film.id }?.isWatchlisted ?: false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerLow)
            .border(0.5.dp, OutlineVariant.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("indie_card_${film.id}")
    ) {
        // Poster Box with Aspect Ratio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(SurfaceContainerLowest)
        ) {
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Scrim Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Festival Laurel or Award Tag at Top
            if (film.festivalLaurel != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1B1B20).copy(alpha = 0.92f))
                        .border(0.5.dp, GoldSecondary.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldSecondary,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = film.festivalLaurel,
                            color = GoldSecondary,
                            fontSize = if (isCompact) 8.sp else 9.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                // Quality Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceContainerLowest.copy(alpha = 0.85f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = film.streamingQuality.take(8),
                        color = OnSurfaceVariant,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Bookmark / Watchlist Action (Top Right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            ) {
                IconButton(
                    onClick = onBookmark,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("bookmark_${film.id}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.65f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isWatchlisted) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Watchlist",
                            tint = if (isWatchlisted) GoldSecondary else OnSurface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom Poster Overlay: Rating and Quick Play
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%.1f", film.rating),
                        color = OnSurface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Play Button
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(CrimsonPrimary)
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Film",
                        tint = OnPrimaryContainer,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Details below Poster
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = film.title,
                color = OnSurface,
                fontSize = if (isCompact) 12.sp else 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Dir. ${film.director}",
                color = CyanTertiary,
                fontSize = if (isCompact) 10.sp else 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${film.year}",
                    color = Outline,
                    fontSize = 10.sp
                )
                Text(text = "•", color = Outline, fontSize = 9.sp)
                Text(
                    text = film.duration,
                    color = Outline,
                    fontSize = 10.sp
                )
            }

            if (!isCompact && film.criticsConsensus != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "“${film.criticsConsensus}”",
                    color = OnSurfaceVariant,
                    fontSize = 10.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    lineHeight = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Featured Spotlight Banner for headline indie laureate.
 */
@Composable
fun IndieSpotlightBanner(
    film: IndieMovieDto,
    onClick: () -> Unit,
    onPlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHigh)
            .border(1.dp, CrimsonPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("indie_spotlight_banner")
    ) {
        // Backdrop Image
        AsyncImage(
            model = film.backdropUrl ?: film.posterUrl,
            contentDescription = film.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        // Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            SurfaceContainerHigh.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .align(Alignment.BottomStart)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoldSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CURATOR'S INDIE SPOTLIGHT",
                    color = GoldSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = film.title,
                color = OnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "${film.festivalLaurel ?: film.budgetTier} • Dir. ${film.director}",
                color = PrimaryLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(CrimsonPrimary)
                            .clickable { onPlay() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = OnPrimaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Watch Film",
                                color = OnPrimaryContainer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "★ ${film.rating}",
                        color = GoldSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onClick() }
                ) {
                    Text(
                        text = "Explore Dossier",
                        color = CyanTertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = CyanTertiary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

/**
 * Shimmering skeleton grid shown while Retrofit network call executes.
 */
@Composable
fun IndieLoadingGridSkeleton() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            SurfaceContainerHigh,
            SurfaceContainerHighest,
            SurfaceContainerHigh
        ),
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // Skeleton Header
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(240.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmerBrush)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Skeleton Grid Cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(6) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerLow)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f / 3f)
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(14.dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(10.dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Error state with retry action
 */
@Composable
fun IndieErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("indie_error_view"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = null,
            tint = CrimsonPrimary,
            modifier = Modifier.size(52.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Indie Ingest Pipeline Disconnected",
            color = OnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = errorMessage,
            color = Outline,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(CrimsonPrimary)
                .clickable { onRetry() }
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .testTag("retry_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = OnPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Retry Retrofit Connection",
                    color = OnPrimaryContainer,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Empty state when search or filter returns 0 results
 */
@Composable
fun IndieEmptyView(
    query: String,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Outline,
            modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No Indie Films Found",
            color = OnSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (query.isNotBlank()) "No films found matching \"$query\" by title or director" else "Try selecting another category or resetting filters",
            color = Outline,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceContainerHigh)
                .clickable { onReset() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("reset_empty_search_button")
        ) {
            Text(
                text = if (query.isNotBlank()) "Clear Search Filter" else "Reset Indie Catalog",
                color = CyanTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
