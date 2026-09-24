package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.WatchlistEntity
import com.example.data.repository.MovieRepository
import com.example.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WatchlistUiState(
    val films: List<WatchlistEntity> = emptyList(),
    val totalCount: Int = 0,
    val searchQuery: String = "",
    val activeFilter: String = "All",
    val sortOrder: String = "recent", // recent, rating, year, title
    val isLoading: Boolean = false
)

class WatchlistViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: WatchlistRepository = MovieRepository.watchlistRepository ?: run {
        val db = AppDatabase.getDatabase(application)
        WatchlistRepository(db.watchlistDao())
    }

    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow("All")
    private val _sortOrder = MutableStateFlow("recent")

    val uiState: StateFlow<WatchlistUiState> = combine(
        repository.allWatchlist,
        _searchQuery,
        _activeFilter,
        _sortOrder
    ) { allFilms, query, filter, sort ->
        var filtered = allFilms

        // 1. Filter by category
        if (filter != "All") {
            filtered = when (filter) {
                "4K UHD" -> filtered.filter { it.videoQuality.contains("4K", ignoreCase = true) }
                "★ 8.5+ Rated" -> filtered.filter { it.rating >= 8.5 }
                "Festival Laureates" -> filtered.filter { !it.awards.isNullOrBlank() }
                else -> filtered.filter { it.genre.contains(filter, ignoreCase = true) }
            }
        }

        // 2. Filter by search query
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            filtered = filtered.filter { film ->
                film.title.lowercase().contains(q) ||
                film.director.lowercase().contains(q) ||
                film.genre.lowercase().contains(q) ||
                film.year.toString().contains(q)
            }
        }

        // 3. Sort
        filtered = when (sort) {
            "rating" -> filtered.sortedByDescending { it.rating }
            "year" -> filtered.sortedByDescending { it.year }
            "title" -> filtered.sortedBy { it.title.lowercase() }
            else -> filtered.sortedByDescending { it.savedAt }
        }

        WatchlistUiState(
            films = filtered,
            totalCount = allFilms.size,
            searchQuery = query,
            activeFilter = filter,
            sortOrder = sort,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WatchlistUiState(isLoading = true)
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: String) {
        _activeFilter.value = filter
    }

    fun onSortOrderSelected(sort: String) {
        _sortOrder.value = sort
    }

    fun removeFilm(movieId: String) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movieId)
            MovieRepository.toggleWatchlist(movieId)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
