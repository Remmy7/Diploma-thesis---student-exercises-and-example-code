package com.example.cv4_ukazka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ExpenseCategory(
    val name: String,
    val amount: Double,
    val budget: Double
)

data class ExpenseTransaction(
    val title: String,
    val category: String,
    val amount: Double,
    val date: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseOverviewScreen()
                }
            }
        }
    }
}

@Composable
fun ExpenseOverviewScreen() {
    val categories = listOf(
        ExpenseCategory("Food", 185.40, 250.00),
        ExpenseCategory("Transport", 72.90, 120.00),
        ExpenseCategory("Shopping", 143.20, 180.00)
    )

    val transactions = listOf(
        ExpenseTransaction("Groceries", "Food", 48.30, "Today"),
        ExpenseTransaction("Bus ticket", "Transport", 16.00, "Yesterday"),
        ExpenseTransaction("New headphones", "Shopping", 79.90, "Monday"),
        ExpenseTransaction("Lunch", "Food", 12.50, "Monday")
    )

    val totalSpent = categories.sumOf { it.amount }
    val totalBudget = categories.sumOf { it.budget }
    val remainingBudget = totalBudget - totalSpent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExpenseHeader()

        BudgetSummaryCard(
            totalSpent = totalSpent,
            totalBudget = totalBudget,
            remainingBudget = remainingBudget
        )

        CategorySection(categories = categories)

        RecentTransactionsSection(transactions = transactions)

        ExpenseActionsSection()
    }
}

@Composable
fun ExpenseHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Expense Overview",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Review monthly spending, category progress, and recent transactions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BudgetSummaryCard(
    totalSpent: Double,
    totalBudget: Double,
    remainingBudget: Double
) {
    val progress = (totalSpent / totalBudget).toFloat().coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Monthly summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "€${formatMoney(totalSpent)} spent of €${formatMoney(totalBudget)}",
                style = MaterialTheme.typography.bodyLarge
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryValueCard(
                    value = "€${formatMoney(totalSpent)}",
                    label = "Spent",
                    modifier = Modifier.weight(1f)
                )
                SummaryValueCard(
                    value = "€${formatMoney(remainingBudget)}",
                    label = "Remaining",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SummaryValueCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun CategorySection(categories: List<ExpenseCategory>) {
    SectionCard(title = "Categories") {
        categories.forEach { category ->
            CategoryBudgetRow(category = category)
        }
    }
}

@Composable
fun CategoryBudgetRow(category: ExpenseCategory) {
    val progress = (category.amount / category.budget).toFloat().coerceIn(0f, 1f)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "€${formatMoney(category.amount)} / €${formatMoney(category.budget)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<ExpenseTransaction>) {
    SectionCard(title = "Recent transactions") {
        transactions.forEach { transaction ->
            TransactionRow(transaction = transaction)
        }
    }
}

@Composable
fun TransactionRow(transaction: ExpenseTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${transaction.category} • ${transaction.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "-€${formatMoney(transaction.amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ExpenseActionsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) {
            Text("Add expense")
        }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) {
            Text("Export")
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            content()
        }
    }
}

private fun formatMoney(value: Double): String {
    return String.format("%.2f", value)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseOverviewScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ExpenseOverviewScreen()
        }
    }
}