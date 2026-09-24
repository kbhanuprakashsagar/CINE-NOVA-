package com.example.data.repository

import android.util.Log
import com.example.data.network.IndieFilmApiService
import com.example.data.network.IndieMovieDto
import com.example.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository coordinating network requests via Retrofit to fetch indie films.
 */
class IndieFilmRepository(
    private val apiService: IndieFilmApiService = RetrofitClient.apiService
) {

    suspend fun getIndieFilms(
        category: String? = null,
        query: String? = null,
        sort: String? = null
    ): Result<List<IndieMovieDto>> = withContext(Dispatchers.IO) {
        try {
            val films = apiService.getIndieFilms(
                category = category,
                query = query,
                sort = sort
            )

            // Cache/register into core MovieRepository so clicks to Detail/Player screens resolve
            val domainMovies = films.map { it.toMovie() }
            MovieRepository.addMoviesIfNotExist(domainMovies)

            Result.success(films)
        } catch (e: Exception) {
            println("IndieFilmRepository: Error fetching indie films via Retrofit: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getIndieFilmById(id: String): Result<IndieMovieDto> = withContext(Dispatchers.IO) {
        try {
            val film = apiService.getIndieFilmById(id)
            MovieRepository.addMoviesIfNotExist(listOf(film.toMovie()))
            Result.success(film)
        } catch (e: Exception) {
            println("IndieFilmRepository: Error fetching indie film by id via Retrofit: ${e.message}")
            Result.failure(e)
        }
    }
}
