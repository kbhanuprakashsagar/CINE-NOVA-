package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Movie

/**
 * Room Entity representing a film saved to the user's local Watchlist database.
 */
@Entity(tableName = "watchlist_films")
data class WatchlistEntity(
    @PrimaryKey
    val movieId: String,
    val title: String,
    val subtitle: String = "",
    val posterUrl: String,
    val backdropUrl: String = "",
    val rating: Double,
    val year: Int,
    val language: String,
    val genre: String,
    val duration: String,
    val videoQuality: String = "4K UHD",
    val audioSpec: String = "Dolby Atmos",
    val synopsis: String,
    val director: String,
    val awards: String? = null,
    val savedAt: Long = System.currentTimeMillis()
) {
    fun toMovie(): Movie {
        return Movie(
            id = movieId,
            title = title,
            subtitle = subtitle,
            posterUrl = posterUrl,
            backdropUrl = backdropUrl.ifEmpty { posterUrl },
            rating = rating,
            year = year,
            language = language,
            genres = listOf(genre),
            duration = duration,
            videoQuality = videoQuality,
            audioSpec = audioSpec,
            synopsis = synopsis,
            director = director,
            awards = awards,
            isWatchlisted = true
        )
    }

    companion object {
        fun fromMovie(movie: Movie): WatchlistEntity {
            return WatchlistEntity(
                movieId = movie.id,
                title = movie.title,
                subtitle = movie.subtitle,
                posterUrl = movie.posterUrl,
                backdropUrl = movie.backdropUrl,
                rating = movie.rating,
                year = movie.year,
                language = movie.language,
                genre = movie.genres.firstOrNull() ?: "Cinema",
                duration = movie.duration,
                videoQuality = movie.videoQuality,
                audioSpec = movie.audioSpec,
                synopsis = movie.synopsis,
                director = movie.director,
                awards = movie.awards,
                savedAt = System.currentTimeMillis()
            )
        }
    }
}
