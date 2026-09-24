package com.example.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for fetching indie cinema catalog, festival selections,
 * and auteur features.
 */
interface IndieFilmApiService {

    @GET("api/v1/films/indie")
    suspend fun getIndieFilms(
        @Query("category") category: String? = null,
        @Query("query") query: String? = null,
        @Query("sort") sort: String? = null
    ): List<IndieMovieDto>

    @GET("api/v1/films/indie/{id}")
    suspend fun getIndieFilmById(
        @Path("id") filmId: String
    ): IndieMovieDto
}
