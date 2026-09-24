package com.example.data.model

data class Movie(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val posterUrl: String,
    val backdropUrl: String = "",
    val rating: Double,
    val voteCount: String = "10K",
    val year: Int,
    val language: String,
    val genres: List<String>,
    val duration: String,
    val videoQuality: String = "4K UHD",
    val audioSpec: String = "Dolby Atmos",
    val synopsis: String,
    val director: String,
    val directorRole: String = "Director • Screenplay",
    val cast: List<CastMember> = emptyList(),
    val awards: String? = null,
    val isTrending: Boolean = false,
    val isOriginal: Boolean = false,
    val isFestivalExclusive: Boolean = false,
    val festPickLabel: String? = null,
    val matchPercentage: Int = 94,
    val progress: Float = 0f, // 0.0 to 1.0
    val timeLeft: String? = null,
    val downloadSize: String = "2.8 GB",
    val isDownloaded: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val isWatchlisted: Boolean = false,
    val userRating: Double? = null,
    val cinephileReview: String? = null,
    val tags: List<String> = emptyList(),
    val boxOfficeMilestone: String? = null,
    val chapterTitle: String? = null,
    val availableLanguages: List<String> = listOf("Malayalam (Original)", "Tamil", "Telugu", "Hindi", "Kannada"),
    val availableSubtitles: List<String> = listOf("English [CC]", "Malayalam", "Hindi", "Arabic", "French")
)

data class CastMember(
    val name: String,
    val role: String,
    val avatarUrl: String? = null
)

data class TrailerExtra(
    val title: String,
    val duration: String,
    val type: String,
    val thumbnailUrl: String
)
