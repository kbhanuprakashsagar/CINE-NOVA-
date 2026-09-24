package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.dao.ReviewDao
import com.example.data.local.entity.ReviewEntity
import com.example.data.repository.ReviewRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReviewsRoomDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: ReviewDao
    private lateinit var repository: ReviewRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase.createInMemoryDatabase(context)
        dao = database.reviewDao()
        repository = ReviewRepository(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveTextReview() = runBlocking {
        val review = ReviewEntity(
            filmId = "bramayugam",
            filmTitle = "Bramayugam",
            director = "Rahul Sadasivan",
            posterUrl = "https://example.com/bramayugam.jpg",
            rating = 5.0f,
            reviewText = "A masterpiece in Indian monochrome folk horror. Mammootty's performance is legendary.",
            reviewerName = "Kiran Dev",
            tags = "Folk Horror • Monochrome"
        )

        val insertedId = dao.insertReview(review)
        assertTrue(insertedId > 0)

        val allReviews = dao.getAllReviews().first()
        assertEquals(1, allReviews.size)

        val retrieved = allReviews.first()
        assertEquals("bramayugam", retrieved.filmId)
        assertEquals("Bramayugam", retrieved.filmTitle)
        assertEquals("Rahul Sadasivan", retrieved.director)
        assertEquals(5.0f, retrieved.rating, 0.01f)
        assertEquals("A masterpiece in Indian monochrome folk horror. Mammootty's performance is legendary.", retrieved.reviewText)
        assertEquals("Kiran Dev", retrieved.reviewerName)
    }

    @Test
    fun testFilterReviewsByFilmId() = runBlocking {
        val review1 = ReviewEntity(
            filmId = "all_we_imagine_as_light",
            filmTitle = "All We Imagine as Light",
            director = "Payal Kapadia",
            rating = 5.0f,
            reviewText = "Breathtaking cinematography and poignant narrative.",
            reviewerName = "Critic A"
        )
        val review2 = ReviewEntity(
            filmId = "bramayugam",
            filmTitle = "Bramayugam",
            director = "Rahul Sadasivan",
            rating = 4.5f,
            reviewText = "Eerie and chilling atmosphere.",
            reviewerName = "Critic B"
        )
        val review3 = ReviewEntity(
            filmId = "all_we_imagine_as_light",
            filmTitle = "All We Imagine as Light",
            director = "Payal Kapadia",
            rating = 4.8f,
            reviewText = "Wonderful pacing and emotional resonance.",
            reviewerName = "Critic C"
        )

        dao.insertAll(listOf(review1, review2, review3))

        val lightReviews = dao.getReviewsForFilm("all_we_imagine_as_light").first()
        assertEquals(2, lightReviews.size)
        assertTrue(lightReviews.all { it.filmId == "all_we_imagine_as_light" })

        val bramayugamReviews = dao.getReviewsForFilm("bramayugam").first()
        assertEquals(1, bramayugamReviews.size)
        assertEquals("Critic B", bramayugamReviews.first().reviewerName)
    }

    @Test
    fun testDeleteReviewById() = runBlocking {
        val review = ReviewEntity(
            filmId = "aadujeevitham",
            filmTitle = "Aadujeevitham - The Goat Life",
            director = "Blessy",
            rating = 4.9f,
            reviewText = "Unrelenting spiritual endurance and incredible score by A.R. Rahman.",
            reviewerName = "Film Scholar"
        )

        val id = dao.insertReview(review)
        val countBefore = dao.getReviewCountSync()
        assertEquals(1, countBefore)

        dao.deleteReviewById(id)
        val countAfter = dao.getReviewCountSync()
        assertEquals(0, countAfter)
    }

    @Test
    fun testRepositoryAddReview() = runBlocking {
        val id = repository.addReview(
            filmId = "kantara",
            filmTitle = "Kantara: Legend",
            director = "Rishab Shetty",
            posterUrl = "https://example.com/kantara.jpg",
            rating = 4.7f,
            reviewText = "Visceral folklore and electrifying final act.",
            reviewerName = "Arjun R."
        )

        assertTrue(id > 0)
        val allReviews = repository.allReviews.first()
        assertEquals(1, allReviews.size)
        assertEquals("kantara", allReviews.first().filmId)
        assertEquals("Arjun R.", allReviews.first().reviewerName)
    }
}
