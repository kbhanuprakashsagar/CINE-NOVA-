package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local film reviews persistence in Room.
 */
@Dao
interface ReviewDao {

    @Query("SELECT * FROM film_reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM film_reviews WHERE filmId = :filmId ORDER BY createdAt DESC")
    fun getReviewsForFilm(filmId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM film_reviews WHERE id = :id LIMIT 1")
    fun getReviewById(id: Long): Flow<ReviewEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("DELETE FROM film_reviews WHERE id = :id")
    suspend fun deleteReviewById(id: Long)

    @Query("SELECT COUNT(*) FROM film_reviews")
    fun getReviewCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM film_reviews")
    suspend fun getReviewCountSync(): Int

    @Query("SELECT COUNT(*) FROM film_reviews WHERE filmId = :filmId")
    fun getReviewCountForFilm(filmId: String): Flow<Int>

    @Query("DELETE FROM film_reviews")
    suspend fun clearAll()
}
