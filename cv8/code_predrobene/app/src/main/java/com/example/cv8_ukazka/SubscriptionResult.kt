package com.example.cv8_ukazka

data class SubscriptionResult(
    val plan: SubscriptionPlan,
    val months: Int,
    val subtotal: Double,
    val discountPercent: Int,
    val finalPrice: Double
)
