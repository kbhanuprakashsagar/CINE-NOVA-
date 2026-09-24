package com.example.data.model

data class LicenseContract(
    val id: String,
    val title: String,
    val languageYear: String,
    val posterUrl: String,
    val rightsOwner: String,
    val studioSubsidiary: String,
    val territory: String,
    val territoryNote: String,
    val licenseModel: String,
    val windowNote: String,
    val windowDates: String,
    val daysRemaining: Int,
    val technicalRights: String,
    val isDownloadAllowed: Boolean,
    val status: String,
    val isExpiringCritical: Boolean = false
)

data class StudioOpsLog(
    val title: String,
    val description: String,
    val timeAgo: String,
    val type: String
)

data class TopStreamingAsset(
    val rank: Int,
    val title: String,
    val studio: String,
    val language: String,
    val streams: String,
    val completion: Int,
    val qualityTier: String
)
