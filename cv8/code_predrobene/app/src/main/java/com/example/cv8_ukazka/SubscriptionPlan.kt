package com.example.cv8_ukazka

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val monthlyPrice: Double,
    val quarterlyDiscount: Int,
    val halfYearDiscount: Int,
    val yearlyDiscount: Int
)

val subscriptionPlans = listOf(
    SubscriptionPlan(
        id = "basic",
        name = "Basic",
        monthlyPrice = 5.99,
        quarterlyDiscount = 5,
        halfYearDiscount = 10,
        yearlyDiscount = 15
    ),
    SubscriptionPlan(
        id = "standard",
        name = "Standard",
        monthlyPrice = 9.99,
        quarterlyDiscount = 8,
        halfYearDiscount = 13,
        yearlyDiscount = 20
    ),
    SubscriptionPlan(
        id = "premium",
        name = "Premium",
        monthlyPrice = 14.99,
        quarterlyDiscount = 10,
        halfYearDiscount = 18,
        yearlyDiscount = 25
    )
)
