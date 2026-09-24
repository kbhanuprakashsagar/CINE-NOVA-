package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.local.entity.WatchlistEntity
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.viewmodel.WatchlistViewModel

/**
 * 'Watchlist' Composable:
 * Displays user's saved favorite films persisted inside a local Room database.
 */
@Composable
fun Watchlist(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToDiscover: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WatchlistViewModel = viewModel()
) {
    WatchlistScreen(
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToPlayer = onNavigateToPlayer,
        onNavigateToDiscover = onNavigateToDiscover,
        onBack = onBack,
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
fun WatchlistScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToDiscover: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WatchlistViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isGridMode by remember { mutableStateOf(true) }
    var showSearchBar by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .statusBarsPadding()
            .testTag("watchlist_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Screen Header
            WatchlistHeader(
                totalCount = uiState.totalCount,
                showSearchBar = showSearchBar,
                isGridMode = isGridMode,
                onBack = onBack,
                onToggleSearch = { showSearchBar = !showSearchBar },
                onToggleGridMode = { isGridMode = !isGridMode },
                onClearAll = { showClearDialog = true }
            )

            // 2. Search Field (Animated Visibility)
            AnimatedVisibility(visible = showSearchBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Filter saved films by title, director...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = CyanTertiary)
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear search", tint = Outline)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanTertiary,
                            unfocusedBorderColor = OutlineVariant,
                            focusedTextColor = OnSurface,
                            unfocusedTextColor = OnSurface,
                            cursorColor = CyanTertiary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("watchlist_search_input")
                    )
                }
            }

            // 3. Category & Filter Chips
            WatchlistFilterChips(
                activeFilter = uiState.activeFilter,
                sortOrder = uiState.sortOrder,
                onFilterSelected = { viewModel.onFilterSelected(it) },
                onSortOrderSelected = { viewModel.onSortOrderSelected(it) }
            )

            // 4. Content Area: Grid / List / Empty State
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CrimsonPrimary)
                }
            } else if (uiState.films.isEmpty()) {
                WatchlistEmptyState(
                    hasQuery = uiState.searchQuery.isNotEmpty() || uiState.activeFilter != "All",
                    onExplore = onNavigateToDiscover,
                    onResetFilter = {
                        viewModel.onSearchQueryChanged("")
                        viewModel.onFilterSelected("All")
                    }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (isGridMode) 2 else 1),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("watchlist_grid"),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = uiState.films,
                        key = { it.movieId }
                    ) { film ->
                        if (isGridMode) {
                            WatchlistGridCard(
                                film = film,
                                onClick = { onNavigateToDetail(film.movieId) },
                                onPlay = { onNavigateToPlayer(film.movieId) },
                                onRemove = { viewModel.removeFilm(film.movieId) }
                            )
                        } else {
                            WatchlistListRowCard(
                                film = film,
                                onClick = { onNavigateToDetail(film.movieId) },
                                onPlay = { onNavigateToPlayer(film.movieId) },
                                onRemove = { viewModel.removeFilm(film.movieId) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmation Dialog for Clearing Watchlist
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = SurfaceContainerHigh,
            title = {
                Text(
                    text = "Clear Watchlist?",
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will remove all saved films from your local Room database. This action cannot be undone.",
                    color = OnSurfaceVariant,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAll()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear All", color = CrimsonPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = Outline)
                }
            }
        )
    }
}

@Composable
private fun WatchlistHeader(
    totalCount: Int,
    showSearchBar: Boolean,
    isGridMode: Boolean,
    onBack: () -> Unit,
    onToggleSearch: () -> Unit,
    onToggleGridMode: () -> Unit,
    onClearAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("watchlist_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = OnSurface
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Watchlist",
                        color = OnSurface,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("watchlist_title")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CrimsonPrimary.copy(alpha = 0.2f))
                            .border(1.dp, CrimsonPrimary.copy(alpha = 0.5f), CircleShape)
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$totalCount",
                            color = CrimsonPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Room Database Persistent Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = "Room Local Database",
                        tint = CyanTertiary,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ROOM PERSISTENT DATABASE",
                        color = CyanTertiary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Action Buttons: Search, Grid/List Switcher, Clear
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onToggleSearch,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Search Watchlist",
                    tint = if (showSearchBar) CrimsonPrimary else OnSurface
                )
            }

            IconButton(
                onClick = onToggleGridMode,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = if (isGridMode) Icons.Default.ViewList else Icons.Default.GridView,
                    contentDescription = "Toggle Grid/List",
                    tint = OnSurface
                )
            }

            if (totalCount > 0) {
                IconButton(
                    onClick = onClearAll,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("clear_watchlist_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Watchlist",
                        tint = Outline
                    )
                }
            }
        }
    }
}

@Composable
private fun WatchlistFilterChips(
    activeFilter: String,
    sortOrder: String,
    onFilterSelected: (String) -> Unit,
    onSortOrderSelected: (String) -> Unit
) {
    val filters = listOf("All", "★ 8.5+ Rated", "4K UHD", "Festival Laureates", "Survival Drama", "Folk Horror")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = activeFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) CrimsonPrimary else SurfaceContainerHigh)
                        .clickable { onFilterSelected(filter) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else OnSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Sort Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Vault Items",
                color = Outline,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val sortOptions = listOf("recent" to "Recent", "rating" to "★ Rating", "year" to "Year")
                sortOptions.forEach { (key, label) ->
                    val isSortActive = sortOrder == key
                    Text(
                        text = label,
                        color = if (isSortActive) GoldSecondary else Outline,
                        fontWeight = if (isSortActive) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .clickable { onSortOrderSelected(key) }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 2-Column Grid Card for Watchlist
 */
@Composable
private fun WatchlistGridCard(
    film: WatchlistEntity,
    onClick: () -> Unit,
    onPlay: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("watchlist_item_${film.movieId}")
    ) {
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

            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Remove Button (Top Right)
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f))
                    .testTag("remove_watchlist_${film.movieId}")
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkRemove,
                    contentDescription = "Remove from Watchlist",
                    tint = CrimsonPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Year & Rating (Bottom)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${film.rating}",
                        color = OnSurface,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Play Button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(CrimsonPrimary)
                        .clickable { onPlay() }
                        .testTag("play_watchlist_${film.movieId}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Film",
                        tint = OnPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Information below poster
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = film.title,
                color = OnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Dir. ${film.director}",
                color = CyanTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${film.genre} • ${film.year}",
                color = Outline,
                fontSize = 10.sp
            )
        }
    }
}

/**
 * 1-Column List Row Card for Watchlist
 */
@Composable
private fun WatchlistListRowCard(
    film: WatchlistEntity,
    onClick: () -> Unit,
    onPlay: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerLow)
            .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp)
            .testTag("watchlist_item_${film.movieId}"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster
        Box(
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainerLowest)
        ) {
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = film.title,
                color = OnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Directed by ${film.director}",
                color = CyanTertiary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldSecondary, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "${film.rating}", color = OnSurface, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Text(text = "•", color = Outline, fontSize = 10.sp)
                Text(text = "${film.year}", color = Outline, fontSize = 11.sp)
                Text(text = "•", color = Outline, fontSize = 10.sp)
                Text(text = film.duration, color = Outline, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = film.synopsis,
                color = OnSurfaceVariant,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Actions
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onPlay,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CrimsonPrimary)
                    .testTag("play_watchlist_${film.movieId}")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Film",
                    tint = OnPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("remove_watchlist_${film.movieId}")
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkRemove,
                    contentDescription = "Remove from Watchlist",
                    tint = Outline
                )
            }
        }
    }
}

/**
 * Empty state when no films are saved in the Watchlist Room database.
 */
@Composable
private fun WatchlistEmptyState(
    hasQuery: Boolean,
    onExplore: () -> Unit,
    onResetFilter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .testTag("empty_watchlist_view"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(1.dp, GoldSecondary.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "Empty Watchlist",
                tint = GoldSecondary,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (hasQuery) "No Matching Saved Films" else "Your Watchlist is Empty",
            color = OnSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (hasQuery)
                "Try changing your search term or category filters to find saved titles."
            else
                "Save your favorite independent features, Cannes laureates, and regional cinema to your local Room database for quick offline tracking.",
            color = OnSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (hasQuery) {
            OutlinedButton(
                onClick = onResetFilter,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Outline)
            ) {
                Text("Reset Filters", color = OnSurface)
            }
        } else {
            Button(
                onClick = onExplore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrimsonPrimary,
                    contentColor = OnPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("explore_films_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Explore Indie Films",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
