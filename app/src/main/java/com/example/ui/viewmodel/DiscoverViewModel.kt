package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.network.IndieMovieDto
import com.example.data.repository.IndieFilmRepository
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DiscoverUiState {
    object Loading : DiscoverUiState
    data class Success(
        val films: List<IndieMovieDto>,
        val totalCount: Int,
        val activeCategory: String,
        val searchQuery: String,
        val sortOrder: String,
        val isThreeColumns: Boolean = false,
        val apiLatencyMs: Long = 120,
        val isRefreshing: Boolean = false
    ) : DiscoverUiState
    data class Error(val message: String) : DiscoverUiState
}

class DiscoverViewModel(
    private val repository: IndieFilmRepository = IndieFilmRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private var currentCategory: String = "All Indie"
    private var currentQuery: String = ""
    private var currentSort: String = "default"
    private var isThreeColumns: Boolean = false

    val categories = listOf(
        "All Indie",
        "Festival Winners",
        "Auteur Cinema",
        "Monochrome & Noir",
        "Malayalam Auteur",
        "Tamil Realism"
    )

    init {
        loadFilms()
    }

    fun loadFilms(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh && _uiState.value !is DiscoverUiState.Success) {
                _uiState.value = DiscoverUiState.Loading
            } else if (isRefresh && _uiState.value is DiscoverUiState.Success) {
                val current = _uiState.value as DiscoverUiState.Success
                _uiState.value = current.copy(isRefreshing = true)
            }

            val startTime = System.currentTimeMillis()
            val result = repository.getIndieFilms(
                category = if (currentCategory == "All Indie") null else currentCategory,
                query = if (currentQuery.isBlank()) null else currentQuery,
                sort = if (currentSort == "default") null else currentSort
            )
            val elapsed = System.currentTimeMillis() - startTime

            result.onSuccess { filmList ->
                _uiState.value = DiscoverUiState.Success(
                    films = filmList,
                    totalCount = filmList.size,
                    activeCategory = currentCategory,
                    searchQuery = currentQuery,
                    sortOrder = currentSort,
                    isThreeColumns = isThreeColumns,
                    apiLatencyMs = elapsed.coerceAtLeast(80),
                    isRefreshing = false
                )
            }.onFailure { error ->
                _uiState.value = DiscoverUiState.Error(
                    message = error.localizedMessage ?: "Failed to connect to Retrofit indie service."
                )
            }
        }
    }

    fun selectCategory(category: String) {
        if (currentCategory == category) return
        currentCategory = category
        loadFilms()
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
        loadFilms()
    }

    fun setSortOrder(sort: String) {
        if (currentSort == sort) return
        currentSort = sort
        loadFilms()
    }

    fun toggleGridColumns() {
        isThreeColumns = !isThreeColumns
        _uiState.update { state ->
            if (state is DiscoverUiState.Success) {
                state.copy(isThreeColumns = isThreeColumns)
            } else state
        }
    }

    fun toggleWatchlist(movieId: String) {
        MovieRepository.toggleWatchlist(movieId)
    }
}
