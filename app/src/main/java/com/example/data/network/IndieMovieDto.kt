package com.example.data.network

import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object representing an independent film fetched via Retrofit.
 */
@JsonClass(generateAdapter = true)
data class IndieMovieDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "original_title") val originalTitle: String? = null,
    @Json(name = "director") val director: String,
    @Json(name = "year") val year: Int,
    @Json(name = "duration") val duration: String,
    @Json(name = "language") val language: String,
    @Json(name = "rating") val rating: Double,
    @Json(name = "poster_url") val posterUrl: String,
    @Json(name = "backdrop_url") val backdropUrl: String? = null,
    @Json(name = "synopsis") val synopsis: String,
    @Json(name = "genres") val genres: List<String> = emptyList(),
    @Json(name = "festival_laurel") val festivalLaurel: String? = null,
    @Json(name = "streaming_quality") val streamingQuality: String = "4K UHD Master",
    @Json(name = "budget_tier") val budgetTier: String = "Independent Feature",
    @Json(name = "critics_consensus") val criticsConsensus: String? = null,
    @Json(name = "cinematographer") val cinematographer: String? = null,
    @Json(name = "tags") val tags: List<String> = emptyList(),
    @Json(name = "cast_names") val castNames: List<String> = emptyList()
) {
    /**
     * Map DTO to the core domain Movie model used across the CineNova application.
     */
    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            subtitle = "${director} • ${year}",
            posterUrl = posterUrl,
            backdropUrl = backdropUrl ?: posterUrl,
            rating = rating,
            voteCount = "Indie Arc",
            year = year,
            language = language,
            genres = genres,
            duration = duration,
            videoQuality = streamingQuality,
            audioSpec = "Dolby Atmos 5.1",
            synopsis = synopsis,
            director = director,
            directorRole = if (cinematographer != null) "Dir: $director • Cinematography: $cinematographer" else "Director • Screenplay",
            cast = castNames.map { name -> CastMember(name = name, role = "Principal Cast") },
            awards = festivalLaurel,
            isTrending = rating >= 8.4,
            isOriginal = true,
            isFestivalExclusive = festivalLaurel != null,
            festPickLabel = festivalLaurel ?: budgetTier,
            matchPercentage = (85 + (rating * 1.5).toInt()).coerceIn(88, 99),
            downloadSize = "2.4 GB",
            tags = tags,
            cinephileReview = criticsConsensus
        )
    }
}
