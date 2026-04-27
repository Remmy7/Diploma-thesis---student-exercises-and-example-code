package com.example.cv8_ukazka

data class SubscriptionUiState(
    val basicMonthsInput: String = "",
    val standardMonthsInput: String = "",
    val premiumMonthsInput: String = "",
    val results: List<SubscriptionResult> = emptyList(),
    val totalBeforeDiscount: Double = 0.0,
    val totalAfterDiscount: Double = 0.0,
    val totalSavings: Double = 0.0
)
