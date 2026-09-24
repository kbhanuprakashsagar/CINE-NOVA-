package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local Watchlist persistence using Room.
 */
@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist_films ORDER BY savedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_films WHERE movieId = :movieId LIMIT 1")
    fun getWatchlistFilm(movieId: String): Flow<WatchlistEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_films WHERE movieId = :movieId)")
    fun isMovieInWatchlist(movieId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_films WHERE movieId = :movieId)")
    suspend fun isMovieInWatchlistSync(movieId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: WatchlistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<WatchlistEntity>)

    @Query("DELETE FROM watchlist_films WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: String)

    @Delete
    suspend fun deleteFilm(film: WatchlistEntity)

    @Query("DELETE FROM watchlist_films")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM watchlist_films")
    fun getWatchlistCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM watchlist_films")
    suspend fun getWatchlistCountSync(): Int
}
