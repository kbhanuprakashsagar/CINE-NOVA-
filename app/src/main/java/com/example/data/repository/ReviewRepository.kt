package com.example.data.repository

import com.example.data.local.dao.ReviewDao
import com.example.data.local.entity.ReviewEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository coordinating Room database operations for Indie Film text reviews.
 */
class ReviewRepository(
    private val reviewDao: ReviewDao
) {
    val allReviews: Flow<List<ReviewEntity>> = reviewDao.getAllReviews()

    val reviewCount: Flow<Int> = reviewDao.getReviewCount()

    fun getReviewsForFilm(filmId: String): Flow<List<ReviewEntity>> {
        return reviewDao.getReviewsForFilm(filmId)
    }

    fun getReviewCountForFilm(filmId: String): Flow<Int> {
        return reviewDao.getReviewCountForFilm(filmId)
    }

    suspend fun addReview(
        filmId: String,
        filmTitle: String,
        director: String,
        posterUrl: String = "",
        rating: Float,
        reviewText: String,
        reviewerName: String = "Cinephile Critic",
        tags: String = "Indie Critique"
    ): Long = withContext(Dispatchers.IO) {
        val review = ReviewEntity(
            filmId = filmId,
            filmTitle = filmTitle,
            director = director,
            posterUrl = posterUrl,
            rating = rating,
            reviewText = reviewText,
            reviewerName = reviewerName.ifBlank { "Cinephile Critic" },
            tags = tags,
            createdAt = System.currentTimeMillis()
        )
        reviewDao.insertReview(review)
    }

    suspend fun insertReview(review: ReviewEntity): Long = withContext(Dispatchers.IO) {
        reviewDao.insertReview(review)
    }

    suspend fun deleteReviewById(id: Long) = withContext(Dispatchers.IO) {
        reviewDao.deleteReviewById(id)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        reviewDao.clearAll()
    }

    suspend fun seedInitialReviewsIfEmpty() = withContext(Dispatchers.IO) {
        val count = reviewDao.getReviewCountSync()
        if (count == 0) {
            val initialReviews = listOf(
                ReviewEntity(
                    filmId = "all_we_imagine_as_light",
                    filmTitle = "All We Imagine as Light",
                    director = "Payal Kapadia",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAjv4zIqFp9jO2pL3N8R_e6A0t7vB2K7mF4P9L6rT1xY_zN8Q3vK0mF",
                    rating = 5.0f,
                    reviewText = "Payal Kapadia's Grand Prix laureate is a transcendent, luminously photographed study of nocturnal intimacy and migratory solitude in contemporary Mumbai. The blue-hour framing is pure visual poetry.",
                    reviewerName = "Ananya S. (Grand Jury Member)",
                    tags = "Cannes Laureate • 35mm Aesthetic",
                    createdAt = System.currentTimeMillis() - 86400000L * 2
                ),
                ReviewEntity(
                    filmId = "aadujeevitham",
                    filmTitle = "Aadujeevitham - The Goat Life",
                    director = "Blessy",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAQvH3dY2u4vJ1w_rV7P6kL0tN8F2mS9X3yK1bT4zL6rT8qW2vK0mF9",
                    rating = 4.8f,
                    reviewText = "Blessy's 16-year magnum opus translates Benyamin's survival epic into an unrelenting spiritual endurance test. Prithviraj's physical transformation and Sunil KS's scorched desert vistas are staggering.",
                    reviewerName = "Karthik Raja (Auteur Club)",
                    tags = "Survival Cinema • A.R. Rahman Score",
                    createdAt = System.currentTimeMillis() - 86400000L * 5
                ),
                ReviewEntity(
                    filmId = "bramayugam",
                    filmTitle = "Bramayugam",
                    director = "Rahul Sadasivan",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA0yP1nL2wK4tV8bX7mF0jQ3zR9yS1bT6kL8qW4zN2vK0mF",
                    rating = 4.5f,
                    reviewText = "A haunting monochrome chamber piece dissecting feudal decay and existential entrapment. Mammootty's Kodumon Potti delivers pure theatrical menace, anchored by atmospheric sound design.",
                    reviewerName = "Devraj Mukherjee",
                    tags = "Folk Horror • Monochrome Masterclass",
                    createdAt = System.currentTimeMillis() - 86400000L * 7
                )
            )
            reviewDao.insertAll(initialReviews)
        }
    }
}
