package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a text-based review left by a user for an indie film.
 * Stored locally in the Room database alongside the user's Watchlist.
 */
@Entity(tableName = "film_reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filmId: String,
    val filmTitle: String,
    val director: String,
    val posterUrl: String = "",
    val rating: Float, // 1.0 to 5.0 scale
    val reviewText: String,
    val reviewerName: String = "Cinephile Critic",
    val tags: String = "Indie Critique",
    val createdAt: Long = System.currentTimeMillis()
)
