package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ReviewEntity
import com.example.data.repository.MovieRepository
import com.example.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FilmItemSummary(
    val id: String,
    val title: String,
    val director: String,
    val posterUrl: String,
    val year: Int
)

data class ReviewsUiState(
    val reviews: List<ReviewEntity> = emptyList(),
    val totalCount: Int = 0,
    val selectedFilmFilter: String? = null,
    val searchQuery: String = "",
    val availableFilms: List<FilmItemSummary> = emptyList(),
    val isWritingReview: Boolean = false,
    val selectedFilmForReview: FilmItemSummary? = null,
    val reviewRating: Float = 5.0f,
    val reviewText: String = "",
    val reviewerName: String = "Cinephile Critic",
    val formError: String? = null,
    val isSubmitting: Boolean = false,
    val isLoading: Boolean = false
)

class ReviewsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ReviewRepository = MovieRepository.reviewRepository ?: run {
        val db = AppDatabase.getDatabase(application)
        ReviewRepository(db.reviewDao())
    }

    private val _selectedFilmFilter = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _isWritingReview = MutableStateFlow(false)
    private val _selectedFilmForReview = MutableStateFlow<FilmItemSummary?>(null)
    private val _reviewRating = MutableStateFlow(5.0f)
    private val _reviewText = MutableStateFlow("")
    private val _reviewerName = MutableStateFlow("Cinephile Critic")
    private val _formError = MutableStateFlow<String?>(null)
    private val _isSubmitting = MutableStateFlow(false)

    // Cached film options from MovieRepository
    private val _availableFilms = MutableStateFlow<List<FilmItemSummary>>(emptyList())

    init {
        loadAvailableFilms()
    }

    private fun loadAvailableFilms() {
        val movies = MovieRepository.movies.value.map {
            FilmItemSummary(
                id = it.id,
                title = it.title,
                director = it.director,
                posterUrl = it.posterUrl,
                year = it.year
            )
        }
        _availableFilms.value = movies
    }

    val uiState: StateFlow<ReviewsUiState> = combine(
        repository.allReviews,
        _selectedFilmFilter,
        _searchQuery,
        _availableFilms,
        _isWritingReview,
        _selectedFilmForReview,
        _reviewRating,
        _reviewText,
        _reviewerName,
        _formError,
        _isSubmitting
    ) { params ->
        val allReviews = params[0] as List<ReviewEntity>
        val filmFilter = params[1] as String?
        val query = params[2] as String
        val films = params[3] as List<FilmItemSummary>
        val isWriting = params[4] as Boolean
        val selectedFilm = params[5] as FilmItemSummary?
        val rating = params[6] as Float
        val text = params[7] as String
        val name = params[8] as String
        val formErr = params[9] as String?
        val submitting = params[10] as Boolean

        var filtered = allReviews

        // 1. Filter by specific film
        if (!filmFilter.isNullOrBlank()) {
            filtered = filtered.filter { it.filmId == filmFilter }
        }

        // 2. Filter by search query (title, director, or review body text)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            filtered = filtered.filter { review ->
                review.filmTitle.lowercase().contains(q) ||
                review.director.lowercase().contains(q) ||
                review.reviewText.lowercase().contains(q) ||
                review.reviewerName.lowercase().contains(q)
            }
        }

        ReviewsUiState(
            reviews = filtered,
            totalCount = allReviews.size,
            selectedFilmFilter = filmFilter,
            searchQuery = query,
            availableFilms = films,
            isWritingReview = isWriting,
            selectedFilmForReview = selectedFilm ?: films.firstOrNull(),
            reviewRating = rating,
            reviewText = text,
            reviewerName = name,
            formError = formErr,
            isSubmitting = submitting,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReviewsUiState(isLoading = true)
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun filterByFilm(filmId: String?) {
        _selectedFilmFilter.value = filmId
    }

    fun openReviewDialog(initialFilmId: String? = null) {
        loadAvailableFilms()
        val films = _availableFilms.value
        val film = if (initialFilmId != null) {
            films.find { it.id == initialFilmId } ?: films.firstOrNull()
        } else {
            films.firstOrNull()
        }
        _selectedFilmForReview.value = film
        _reviewRating.value = 5.0f
        _reviewText.value = ""
        _formError.value = null
        _isWritingReview.value = true
    }

    fun closeReviewDialog() {
        _isWritingReview.value = false
        _formError.value = null
    }

    fun selectFilmForReview(film: FilmItemSummary) {
        _selectedFilmForReview.value = film
    }

    fun setRating(rating: Float) {
        _reviewRating.value = rating
    }

    fun onReviewTextChanged(text: String) {
        _reviewText.value = text
        if (_formError.value != null && text.isNotBlank()) {
            _formError.value = null
        }
    }

    fun onReviewerNameChanged(name: String) {
        _reviewerName.value = name
    }

    fun submitReview(onSuccess: () -> Unit = {}) {
        val film = _selectedFilmForReview.value
        val text = _reviewText.value.trim()

        if (film == null) {
            _formError.value = "Please select an indie film to review."
            return
        }

        if (text.isBlank()) {
            _formError.value = "Review text cannot be blank. Please share your critique."
            return
        }

        _isSubmitting.value = true
        viewModelScope.launch {
            try {
                repository.addReview(
                    filmId = film.id,
                    filmTitle = film.title,
                    director = film.director,
                    posterUrl = film.posterUrl,
                    rating = _reviewRating.value,
                    reviewText = text,
                    reviewerName = _reviewerName.value.ifBlank { "Cinephile Critic" },
                    tags = "Indie Critique • ${film.year}"
                )
                _isWritingReview.value = false
                _reviewText.value = ""
                _formError.value = null
                onSuccess()
            } catch (e: Exception) {
                _formError.value = e.localizedMessage ?: "Failed to save review."
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun deleteReview(reviewId: Long) {
        viewModelScope.launch {
            repository.deleteReviewById(reviewId)
        }
    }
}
