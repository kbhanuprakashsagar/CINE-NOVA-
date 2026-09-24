package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.dao.WatchlistDao
import com.example.data.local.entity.WatchlistEntity
import com.example.data.model.Movie
import com.example.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class WatchlistRoomDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: WatchlistDao
    private lateinit var repository: WatchlistRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase.createInMemoryDatabase(context)
        dao = database.watchlistDao()
        repository = WatchlistRepository(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveWatchlistFilm() = runBlocking {
        val film = WatchlistEntity(
            movieId = "bramayugam",
            title = "Bramayugam: The Age of Madness",
            posterUrl = "https://example.com/poster.jpg",
            rating = 8.5,
            year = 2024,
            language = "Malayalam",
            genre = "Folk Horror",
            duration = "2h 19m",
            synopsis = "17th century Malabar folk horror mystery.",
            director = "Rahul Sadasivan"
        )

        dao.insertFilm(film)

        val retrievedFilms = dao.getAllWatchlist().first()
        assertEquals(1, retrievedFilms.size)
        assertEquals("bramayugam", retrievedFilms.first().movieId)
        assertEquals("Rahul Sadasivan", retrievedFilms.first().director)
        assertEquals(2024, retrievedFilms.first().year)
    }

    @Test
    fun testRepositoryToggleWatchlist() = runBlocking {
        val sampleMovie = Movie(
            id = "all_we_imagine_as_light",
            title = "All We Imagine as Light",
            posterUrl = "https://example.com/poster2.jpg",
            rating = 8.7,
            year = 2024,
            language = "Malayalam",
            genres = listOf("Auteur Drama"),
            duration = "1h 58m",
            synopsis = "Two Kerala nurses in Mumbai journey to a coastal haven.",
            director = "Payal Kapadia"
        )

        // 1. Initial state: not in watchlist
        assertFalse(repository.isMovieInWatchlistSync(sampleMovie.id))

        // 2. Toggle to Add
        val added = repository.toggleWatchlist(sampleMovie)
        assertTrue(added)
        assertTrue(repository.isMovieInWatchlistSync(sampleMovie.id))

        val countAfterAdd = dao.getWatchlistCountSync()
        assertEquals(1, countAfterAdd)

        // 3. Toggle to Remove
        val removed = repository.toggleWatchlist(sampleMovie)
        assertFalse(removed)
        assertFalse(repository.isMovieInWatchlistSync(sampleMovie.id))

        val countAfterRemove = dao.getWatchlistCountSync()
        assertEquals(0, countAfterRemove)
    }

    @Test
    fun testFloatingHeartToggleAddsAndRemovesWithoutNavigating() = runBlocking {
        val indieFilm = Movie(
            id = "thallumaala",
            title = "Thallumaala",
            posterUrl = "https://example.com/thallumaala.jpg",
            rating = 8.1,
            year = 2022,
            language = "Malayalam",
            genres = listOf("Stylized Action", "Hyper-Pop"),
            duration = "2h 28m",
            synopsis = "A non-linear brawl-fest documenting Malabar youth.",
            director = "Khalid Rahman"
        )

        // 1. Initial State: Movie is not in local Room database
        assertEquals(0, dao.getWatchlistCountSync())
        assertFalse(dao.isMovieInWatchlistSync(indieFilm.id))

        // 2. Floating Heart Toggle Click -> Adds to local Room Watchlist
        val entityToAdd = WatchlistEntity.fromMovie(indieFilm)
        dao.insertFilm(entityToAdd)

        // Verify instant Room update
        assertTrue(dao.isMovieInWatchlistSync(indieFilm.id))
        assertEquals(1, dao.getWatchlistCountSync())
        val savedFilm = dao.getAllWatchlist().first().first()
        assertEquals("thallumaala", savedFilm.movieId)
        assertEquals("Thallumaala", savedFilm.title)
        assertEquals("Khalid Rahman", savedFilm.director)

        // 3. Second Floating Heart Toggle Click -> Instantly removes from local Room Watchlist
        dao.deleteByMovieId(indieFilm.id)

        // Verify instant Room removal
        assertFalse(dao.isMovieInWatchlistSync(indieFilm.id))
        assertEquals(0, dao.getWatchlistCountSync())
        assertTrue(dao.getAllWatchlist().first().isEmpty())
    }

    @Test
    fun testClearAllWatchlist() = runBlocking {
        val film1 = WatchlistEntity(
            movieId = "m1",
            title = "Film One",
            posterUrl = "",
            rating = 8.0,
            year = 2023,
            language = "Malayalam",
            genre = "Drama",
            duration = "2h",
            synopsis = "Synopsis 1",
            director = "Director 1"
        )
        val film2 = WatchlistEntity(
            movieId = "m2",
            title = "Film Two",
            posterUrl = "",
            rating = 9.0,
            year = 2024,
            language = "Tamil",
            genre = "Thriller",
            duration = "2h 10m",
            synopsis = "Synopsis 2",
            director = "Director 2"
        )

        dao.insertAll(listOf(film1, film2))
        assertEquals(2, dao.getWatchlistCountSync())

        repository.clearAll()
        assertEquals(0, dao.getWatchlistCountSync())
        assertTrue(dao.getAllWatchlist().first().isEmpty())
    }
}
