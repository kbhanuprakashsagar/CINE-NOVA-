package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.local.entity.ReviewEntity
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
import com.example.ui.viewmodel.FilmItemSummary
import com.example.ui.viewmodel.ReviewsUiState
import com.example.ui.viewmodel.ReviewsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Reviews Composable: Primary screen allowing users to read, filter,
 * and submit text-based reviews for indie films, stored in Room database.
 */
@Composable
fun Reviews(
    onNavigateToDetail: (String) -> Unit,
    onBack: () -> Unit = {},
    initialFilmId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: ReviewsViewModel = viewModel()
) {
    ReviewsScreen(
        onNavigateToDetail = onNavigateToDetail,
        onBack = onBack,
        initialFilmId = initialFilmId,
        modifier = modifier,
        viewModel = viewModel
    )
}

@Composable
fun ReviewsScreen(
    onNavigateToDetail: (String) -> Unit,
    onBack: () -> Unit = {},
    initialFilmId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: ReviewsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf<ReviewEntity?>(null) }

    // If an initialFilmId is provided, filter to it initially
    androidx.compose.runtime.LaunchedEffect(initialFilmId) {
        if (!initialFilmId.isNullOrBlank()) {
            viewModel.filterByFilm(initialFilmId)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .testTag("reviews_screen_root"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openReviewDialog(initialFilmId) },
                containerColor = CrimsonPrimary,
                contentColor = OnPrimaryContainer,
                modifier = Modifier
                    .testTag("write_review_fab")
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Write a Review",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Surface)
                .padding(innerPadding)
        ) {
            // Screen Header & Action Row
            ReviewsHeader(
                totalCount = uiState.totalCount,
                filteredCount = uiState.reviews.size,
                selectedFilm = uiState.availableFilms.find { it.id == uiState.selectedFilmFilter },
                onWriteReviewClick = { viewModel.openReviewDialog(initialFilmId) },
                onBack = onBack
            )

            // Search Bar for Reviews
            ReviewsSearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            // Horizontal Filter Chips of Films
            FilmFilterChips(
                availableFilms = uiState.availableFilms,
                selectedFilmId = uiState.selectedFilmFilter,
                onSelectFilm = { viewModel.filterByFilm(it) }
            )

            // Content List or Empty State
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CrimsonPrimary)
                }
            } else if (uiState.reviews.isEmpty()) {
                ReviewsEmptyState(
                    hasFilter = uiState.selectedFilmFilter != null || uiState.searchQuery.isNotBlank(),
                    onWriteReview = { viewModel.openReviewDialog(uiState.selectedFilmFilter) },
                    onClearFilter = {
                        viewModel.filterByFilm(null)
                        viewModel.onSearchQueryChanged("")
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("reviews_list"),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(uiState.reviews, key = { it.id }) { review ->
                        ReviewCard(
                            review = review,
                            onCardClick = { onNavigateToDetail(review.filmId) },
                            onDeleteClick = { showDeleteConfirmDialog = review }
                        )
                    }
                }
            }
        }

        // Leave Review Dialog
        if (uiState.isWritingReview) {
            WriteReviewDialog(
                uiState = uiState,
                onSelectFilm = { viewModel.selectFilmForReview(it) },
                onRatingChange = { viewModel.setRating(it) },
                onTextChange = { viewModel.onReviewTextChanged(it) },
                onNameChange = { viewModel.onReviewerNameChanged(it) },
                onSubmit = { viewModel.submitReview() },
                onDismiss = { viewModel.closeReviewDialog() }
            )
        }

        // Delete Confirmation Dialog
        showDeleteConfirmDialog?.let { review ->
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = null },
                containerColor = SurfaceContainerHigh,
                title = {
                    Text(
                        text = "Delete Review?",
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to remove your critique for \"${review.filmTitle}\" from the database?",
                        color = OnSurfaceVariant,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteReview(review.id)
                            showDeleteConfirmDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        modifier = Modifier.testTag("confirm_delete_review_button")
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirmDialog = null },
                        modifier = Modifier.testTag("cancel_delete_review_button")
                    ) {
                        Text("Cancel", color = Outline)
                    }
                }
            )
        }
    }
}

@Composable
private fun ReviewsHeader(
    totalCount: Int,
    filteredCount: Int,
    selectedFilm: FilmItemSummary?,
    onWriteReviewClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("reviews_back_button")
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
                            text = "Indie Reviews",
                            color = OnSurface,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CrimsonPrimary.copy(alpha = 0.2f))
                                .border(1.dp, CrimsonPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$totalCount Stored",
                                color = CrimsonPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = if (selectedFilm != null) "Filtered by ${selectedFilm.title}" else "Community auteur critiques & notes in Room DB",
                        color = Outline,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Quick "Write" Button
            Button(
                onClick = onWriteReviewClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrimsonPrimary,
                    contentColor = OnPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier
                    .height(40.dp)
                    .testTag("write_review_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Write",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ReviewsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search reviews by text, film, or director...",
                fontSize = 12.sp,
                color = Outline
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = CyanTertiary,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("clear_reviews_search")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = Outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceContainerHigh,
            unfocusedContainerColor = SurfaceContainerLow,
            focusedBorderColor = CrimsonPrimary,
            unfocusedBorderColor = OutlineVariant.copy(alpha = 0.5f),
            focusedTextColor = OnSurface,
            unfocusedTextColor = OnSurface,
            cursorColor = CrimsonPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("reviews_search_bar")
    )
}

@Composable
private fun FilmFilterChips(
    availableFilms: List<FilmItemSummary>,
    selectedFilmId: String?,
    onSelectFilm: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChipItem(
                label = "All Films",
                isSelected = selectedFilmId == null,
                onClick = { onSelectFilm(null) },
                testTag = "filter_chip_all_films"
            )
        }

        items(availableFilms) { film ->
            FilterChipItem(
                label = film.title,
                isSelected = selectedFilmId == film.id,
                onClick = {
                    if (selectedFilmId == film.id) onSelectFilm(null) else onSelectFilm(film.id)
                },
                testTag = "filter_chip_${film.id}"
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) CrimsonPrimary else SurfaceContainerHigh)
            .border(
                width = 1.dp,
                color = if (isSelected) CrimsonPrimary else OutlineVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Text(
            text = label,
            color = if (isSelected) OnPrimaryContainer else OnSurface,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun ReviewCard(
    review: ReviewEntity,
    onCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val formattedDate = remember(review.createdAt) { dateFormat.format(Date(review.createdAt)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(14.dp))
            .clickable { onCardClick() }
            .testTag("review_card")
            .testTag("review_card_${review.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top Row: Film Info & Poster Thumbnail
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Film Poster Thumbnail
                if (review.posterUrl.isNotBlank()) {
                    AsyncImage(
                        model = review.posterUrl,
                        contentDescription = review.filmTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 46.dp, height = 64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHighest)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(width = 46.dp, height = 64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = Outline,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Title & Director
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.filmTitle,
                        color = OnSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Dir. ${review.director}",
                        color = CyanTertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Star Rating Bar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DisplayStars(rating = review.rating)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%.1f", review.rating),
                            color = GoldSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Delete Button
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("delete_review_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Review",
                        tint = Outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = SurfaceContainerHigh, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Review Body Text
            Text(
                text = review.reviewText,
                color = OnSurface,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("review_body_text")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer: Reviewer Name & Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(CrimsonPrimary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.reviewerName.firstOrNull()?.uppercase() ?: "C",
                            color = CrimsonPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = review.reviewerName,
                        color = OnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = formattedDate,
                    color = Outline,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun DisplayStars(rating: Float) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        val fullStars = rating.toInt().coerceIn(0, 5)
        val hasHalf = (rating - fullStars) >= 0.5f

        for (i in 1..5) {
            when {
                i <= fullStars -> {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                i == fullStars + 1 && hasHalf -> {
                    Icon(
                        imageVector = Icons.Default.StarHalf,
                        contentDescription = null,
                        tint = GoldSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Outline.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WriteReviewDialog(
    uiState: ReviewsUiState,
    onSelectFilm: (FilmItemSummary) -> Unit,
    onRatingChange: (Float) -> Unit,
    onTextChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("write_review_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RateReview,
                        contentDescription = null,
                        tint = CrimsonPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Write Film Critique",
                        color = OnSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Film Selector
                Text(
                    text = "SELECT FILM",
                    color = Outline,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.availableFilms) { film ->
                        val isSelected = uiState.selectedFilmForReview?.id == film.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CrimsonPrimary else SurfaceContainerLow)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) CrimsonPrimary else OutlineVariant,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectFilm(film) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("select_film_${film.id}")
                        ) {
                            Text(
                                text = film.title,
                                color = if (isSelected) OnPrimaryContainer else OnSurface,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Interactive Star Rating Picker (touch target >= 48dp)
                Text(
                    text = "STAR RATING (${String.format(Locale.getDefault(), "%.1f", uiState.reviewRating)} / 5.0)",
                    color = Outline,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_star_rating_row"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { onRatingChange(i.toFloat()) },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("rating_star_$i")
                        ) {
                            Icon(
                                imageVector = if (uiState.reviewRating >= i) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Rate $i stars",
                                tint = if (uiState.reviewRating >= i) GoldSecondary else Outline,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                // Reviewer Name Field
                OutlinedTextField(
                    value = uiState.reviewerName,
                    onValueChange = onNameChange,
                    label = { Text("Your Critic Name / Pen Name", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLow,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = CrimsonPrimary,
                        unfocusedBorderColor = OutlineVariant,
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reviewer_name_input")
                )

                // Review Text Field
                OutlinedTextField(
                    value = uiState.reviewText,
                    onValueChange = onTextChange,
                    label = { Text("Your Critique & Thoughts", fontSize = 12.sp) },
                    placeholder = {
                        Text(
                            text = "Share your impressions on direction, cinematography, performances...",
                            fontSize = 11.sp,
                            color = Outline
                        )
                    },
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLow,
                        unfocusedContainerColor = SurfaceContainerLow,
                        focusedBorderColor = CrimsonPrimary,
                        unfocusedBorderColor = OutlineVariant,
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_text_input")
                )

                // Form error if any
                if (!uiState.formError.isNullOrBlank()) {
                    Text(
                        text = uiState.formError,
                        color = CrimsonPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.testTag("review_form_error")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSubmit,
                enabled = !uiState.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrimsonPrimary,
                    contentColor = OnPrimaryContainer
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("submit_review_button")
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Submit Critique", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_review_button")
            ) {
                Text("Cancel", color = Outline)
            }
        }
    )
}

@Composable
private fun ReviewsEmptyState(
    hasFilter: Boolean,
    onWriteReview: () -> Unit,
    onClearFilter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.RateReview,
                contentDescription = null,
                tint = CrimsonPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasFilter) "No Reviews Matching Filter" else "No Indie Reviews Yet",
            color = OnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (hasFilter) {
                "Try searching for another term or clearing the active film filter."
            } else {
                "Be the first to share your auteur critique! Your reviews are saved locally in the Room database."
            },
            color = Outline,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (hasFilter) {
            OutlinedButton(
                onClick = onClearFilter,
                modifier = Modifier.testTag("clear_filter_button")
            ) {
                Text("Clear Filter", color = CyanTertiary)
            }
        } else {
            Button(
                onClick = onWriteReview,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrimsonPrimary,
                    contentColor = OnPrimaryContainer
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("empty_write_review_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Write First Review", fontWeight = FontWeight.Bold)
            }
        }
    }
}
