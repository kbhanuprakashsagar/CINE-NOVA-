package com.example

import com.example.data.network.RetrofitClient
import com.example.data.repository.IndieFilmRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DiscoverRetrofitTest {

    @Test
    fun testIndieFilmApiService_fetchesFilms() = runBlocking {
        val apiService = RetrofitClient.apiService
        val films = apiService.getIndieFilms()

        assertNotNull(films)
        assertTrue("Expected non-empty list of indie films", films.isNotEmpty())
        val firstFilm = films.first()
        assertTrue("Title should not be blank", firstFilm.title.isNotBlank())
        assertTrue("Director should not be blank", firstFilm.director.isNotBlank())
        assertTrue("Rating should be greater than 0", firstFilm.rating > 0.0)
    }

    @Test
    fun testIndieFilmDto_convertsToDomainMovie() = runBlocking {
        val films = RetrofitClient.apiService.getIndieFilms()
        val filmDto = films.first()
        val domainMovie = filmDto.toMovie()

        assertEquals(filmDto.id, domainMovie.id)
        assertEquals(filmDto.title, domainMovie.title)
        assertEquals(filmDto.director, domainMovie.director)
        assertEquals(filmDto.rating, domainMovie.rating, 0.01)
    }

    @Test
    fun testIndieFilmRepository_fetchesAndRegisters() = runBlocking {
        val repository = IndieFilmRepository()
        val result = repository.getIndieFilms()

        assertTrue(result.isSuccess)
        val films = result.getOrNull()
        assertNotNull(films)
        assertTrue(films!!.isNotEmpty())
    }

    @Test
    fun testIndieFilmCategoryFilter() = runBlocking {
        val apiService = RetrofitClient.apiService
        val festivalFilms = apiService.getIndieFilms(category = "Festival Winners")

        assertTrue(festivalFilms.isNotEmpty())
        festivalFilms.forEach { film ->
            assertNotNull(film.festivalLaurel)
        }
    }

    @Test
    fun testFilmDetail_attributesPresentForGridMovies() = runBlocking {
        val repository = IndieFilmRepository()
        val allFilms = RetrofitClient.apiService.getIndieFilms()
        assertTrue(allFilms.isNotEmpty())

        val film = allFilms.first()
        // Verify key FilmDetail attributes required by user
        assertTrue("Description / synopsis must be present", film.synopsis.isNotBlank())
        assertTrue("Release year must be valid", film.year >= 1900)
        assertTrue("Director must be present", film.director.isNotBlank())

        // Verify fetching single film by ID for FilmDetail screen
        val singleFilmResult = repository.getIndieFilmById(film.id)
        assertTrue(singleFilmResult.isSuccess)
        val singleFilm = singleFilmResult.getOrNull()
        assertNotNull(singleFilm)
        assertEquals(film.id, singleFilm!!.id)
        assertEquals(film.synopsis, singleFilm.synopsis)
        assertEquals(film.year, singleFilm.year)
        assertEquals(film.director, singleFilm.director)
    }

    @Test
    fun testIndieFilmSearch_byTitle() = runBlocking {
        val apiService = RetrofitClient.apiService
        val searchResults = apiService.getIndieFilms(query = "Light")

        assertTrue("Should return search results for 'Light'", searchResults.isNotEmpty())
        assertTrue(searchResults.any { it.title.contains("Light", ignoreCase = true) })
    }

    @Test
    fun testIndieFilmSearch_byDirector() = runBlocking {
        val apiService = RetrofitClient.apiService
        // Filter by director "Payal"
        val payalFilms = apiService.getIndieFilms(query = "Payal")
        assertTrue("Should match films directed by Payal", payalFilms.isNotEmpty())
        assertTrue(payalFilms.all { it.director.contains("Payal", ignoreCase = true) })

        // Filter by director "Rahul"
        val rahulFilms = apiService.getIndieFilms(query = "Rahul")
        assertTrue("Should match films directed by Rahul", rahulFilms.isNotEmpty())
        assertTrue(rahulFilms.all { it.director.contains("Rahul", ignoreCase = true) })
    }
}
