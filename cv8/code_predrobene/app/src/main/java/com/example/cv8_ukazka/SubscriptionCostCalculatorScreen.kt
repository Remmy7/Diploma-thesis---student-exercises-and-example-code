package com.example.cv8_ukazka

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun SubscriptionCostCalculatorScreen(
    uiState: SubscriptionUiState,
    onMonthsChange: (String, String) -> Unit,
    onResetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderSection()

        subscriptionPlans.forEach { plan ->
            val inputValue = when (plan.id) {
                "basic" -> uiState.basicMonthsInput
                "standard" -> uiState.standardMonthsInput
                "premium" -> uiState.premiumMonthsInput
                else -> ""
            }

            val result = uiState.results.firstOrNull { it.plan.id == plan.id }

            SubscriptionPlanCard(
                plan = plan,
                monthsInput = inputValue,
                result = result,
                onMonthsChange = { value ->
                    onMonthsChange(plan.id, value)
                }
            )
        }

        TotalPriceCard(uiState = uiState)

        OutlinedButton(
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset calculator")
        }
    }
}

@Composable
fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Subscription Cost Calculator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter how many months you want to pay for each plan and compare the final price after discounts.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SubscriptionPlanCard(
    plan: SubscriptionPlan,
    monthsInput: String,
    result: SubscriptionResult?,
    onMonthsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "€${formatPrice(plan.monthlyPrice)} per month",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${result?.discountPercent ?: 0}% off",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            DiscountInfoRow(plan = plan)

            OutlinedTextField(
                value = monthsInput,
                onValueChange = onMonthsChange,
                label = { Text("Months") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            result?.let {
                PlanPriceBreakdown(result = it)
            }
        }
    }
}

@Composable
fun DiscountInfoRow(plan: SubscriptionPlan) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DiscountBadge(
            text = "3+ months: ${plan.quarterlyDiscount}%",
            modifier = Modifier.weight(1f)
        )

        DiscountBadge(
            text = "6+ months: ${plan.halfYearDiscount}%",
            modifier = Modifier.weight(1f)
        )

        DiscountBadge(
            text = "12+ months: ${plan.yearlyDiscount}%",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DiscountBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PlanPriceBreakdown(result: SubscriptionResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Selected months: ${result.months}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Before discount: €${formatPrice(result.subtotal)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Final price: €${formatPrice(result.finalPrice)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun TotalPriceCard(uiState: SubscriptionUiState) {
    val hasAnyMonths = uiState.results.any { it.months > 0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Final result",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (hasAnyMonths) {
                Text(
                    text = "Before discount: €${formatPrice(uiState.totalBeforeDiscount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "Saved: €${formatPrice(uiState.totalSavings)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "Final price: €${formatPrice(uiState.totalAfterDiscount)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Text(
                    text = "Enter months for at least one subscription plan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

private fun formatPrice(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubscriptionCostCalculatorScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SubscriptionCostCalculatorScreen(
                uiState = SubscriptionUiState(
                    basicMonthsInput = "3",
                    standardMonthsInput = "6",
                    premiumMonthsInput = "12",
                    results = listOf(
                        SubscriptionResult(
                            plan = subscriptionPlans[0],
                            months = 3,
                            subtotal = 17.97,
                            discountPercent = 5,
                            finalPrice = 17.0715
                        ),
                        SubscriptionResult(
                            plan = subscriptionPlans[1],
                            months = 6,
                            subtotal = 59.94,
                            discountPercent = 13,
                            finalPrice = 52.1478
                        ),
                        SubscriptionResult(
                            plan = subscriptionPlans[2],
                            months = 12,
                            subtotal = 179.88,
                            discountPercent = 25,
                            finalPrice = 134.91
                        )
                    ),
                    totalBeforeDiscount = 257.79,
                    totalAfterDiscount = 204.1293,
                    totalSavings = 53.6607
                ),
                onMonthsChange = { _, _ -> },
                onResetClick = {}
            )
        }
    }
}
