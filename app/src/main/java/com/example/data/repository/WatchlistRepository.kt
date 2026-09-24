package com.example.data.repository

import com.example.data.local.dao.WatchlistDao
import com.example.data.local.entity.WatchlistEntity
import com.example.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Repository abstracting Room local database operations for Watchlist persistence.
 */
class WatchlistRepository(
    private val watchlistDao: WatchlistDao
) {
    val allWatchlist: Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    val allWatchlistMovies: Flow<List<Movie>> = allWatchlist.map { list ->
        list.map { it.toMovie() }
    }

    val watchlistCount: Flow<Int> = watchlistDao.getWatchlistCount()

    fun isMovieWatchlisted(movieId: String): Flow<Boolean> {
        return watchlistDao.isMovieInWatchlist(movieId)
    }

    suspend fun isMovieInWatchlistSync(movieId: String): Boolean = withContext(Dispatchers.IO) {
        watchlistDao.isMovieInWatchlistSync(movieId)
    }

    suspend fun addToWatchlist(movie: Movie) = withContext(Dispatchers.IO) {
        val entity = WatchlistEntity.fromMovie(movie)
        watchlistDao.insertFilm(entity)
    }

    suspend fun removeFromWatchlist(movieId: String) = withContext(Dispatchers.IO) {
        watchlistDao.deleteByMovieId(movieId)
    }

    suspend fun toggleWatchlist(movie: Movie): Boolean = withContext(Dispatchers.IO) {
        val isInWatchlist = watchlistDao.isMovieInWatchlistSync(movie.id)
        if (isInWatchlist) {
            watchlistDao.deleteByMovieId(movie.id)
            false
        } else {
            val entity = WatchlistEntity.fromMovie(movie)
            watchlistDao.insertFilm(entity)
            true
        }
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        watchlistDao.clearAll()
    }

    suspend fun seedInitialFavoritesIfEmpty(movies: List<Movie>) = withContext(Dispatchers.IO) {
        val count = watchlistDao.getWatchlistCountSync()
        if (count == 0) {
            val initialFavorites = movies.filter { it.isWatchlisted }.map { WatchlistEntity.fromMovie(it) }
            if (initialFavorites.isNotEmpty()) {
                watchlistDao.insertAll(initialFavorites)
            }
        }
    }
}
