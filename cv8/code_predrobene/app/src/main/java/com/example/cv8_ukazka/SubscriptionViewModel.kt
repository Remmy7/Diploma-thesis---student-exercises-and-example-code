package com.example.cv8_ukazka

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SubscriptionViewModel : ViewModel() {
    var uiState by mutableStateOf(SubscriptionUiState())
        private set

    init {
        uiState = recalculate(uiState)
    }

    fun onMonthsChange(planId: String, newValue: String) {
        val cleanValue = newValue.filter { it.isDigit() }.take(3)

        val updatedState = when (planId) {
            "basic" -> uiState.copy(basicMonthsInput = cleanValue)
            "standard" -> uiState.copy(standardMonthsInput = cleanValue)
            "premium" -> uiState.copy(premiumMonthsInput = cleanValue)
            else -> uiState
        }

        uiState = recalculate(updatedState)
    }

    fun reset() {
        uiState = recalculate(SubscriptionUiState())
    }

    private fun recalculate(state: SubscriptionUiState): SubscriptionUiState {
        val results = subscriptionPlans.map { plan ->
            val months = when (plan.id) {
                "basic" -> state.basicMonthsInput.toIntOrNull() ?: 0
                "standard" -> state.standardMonthsInput.toIntOrNull() ?: 0
                "premium" -> state.premiumMonthsInput.toIntOrNull() ?: 0
                else -> 0
            }

            val discountPercent = getDiscountPercent(months, plan)
            val subtotal = months * plan.monthlyPrice
            val finalPrice = subtotal * (1 - discountPercent / 100.0)

            SubscriptionResult(
                plan = plan,
                months = months,
                subtotal = subtotal,
                discountPercent = discountPercent,
                finalPrice = finalPrice
            )
        }

        val totalBeforeDiscount = results.sumOf { it.subtotal }
        val totalAfterDiscount = results.sumOf { it.finalPrice }

        return state.copy(
            results = results,
            totalBeforeDiscount = totalBeforeDiscount,
            totalAfterDiscount = totalAfterDiscount,
            totalSavings = totalBeforeDiscount - totalAfterDiscount
        )
    }

    private fun getDiscountPercent(
        months: Int,
        plan: SubscriptionPlan
    ): Int {
        return when {
            months >= 12 -> plan.yearlyDiscount
            months >= 6 -> plan.halfYearDiscount
            months >= 3 -> plan.quarterlyDiscount
            else -> 0
        }
    }
}
