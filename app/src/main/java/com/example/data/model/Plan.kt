package com.example.data.model

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val tag: String,
    val badge: String? = null,
    val monthlyPrice: String,
    val yearlyPrice: String,
    val originalYearlyPrice: String,
    val effectiveMonthly: String,
    val features: List<PlanFeature>,
    val isCurrent: Boolean = false,
    val isPopular: Boolean = false
)

data class PlanFeature(
    val title: String,
    val description: String = "",
    val icon: String = "check"
)

data class PlanSpecRow(
    val feature: String,
    val mobility: String,
    val standard: String,
    val premium: String
)
