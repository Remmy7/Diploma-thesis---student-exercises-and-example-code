package com.example.cv5_ukazka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class PackingItem(
    val id: Int,
    val name: String,
    val isPacked: Boolean
)

enum class PackingFilter {
    ALL,
    UNPACKED,
    PACKED
}

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
                    TravelPackingListScreen()
                }
            }
        }
    }
}

@Composable
fun TravelPackingListScreen() {
    val items = remember {
        mutableStateListOf(
            PackingItem(1, "Passport", false),
            PackingItem(2, "Phone charger", false),
            PackingItem(3, "T-shirts", true),
            PackingItem(4, "Toothbrush", false),
            PackingItem(5, "Sunglasses", true)
        )
    }

    var newItemText by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(6) }
    var selectedFilter by remember { mutableStateOf(PackingFilter.ALL) }

    val filteredItems = items.filter { item ->
        when (selectedFilter) {
            PackingFilter.ALL -> true
            PackingFilter.UNPACKED -> !item.isPacked
            PackingFilter.PACKED -> item.isPacked
        }
    }

    val packedCount = items.count { it.isPacked }
    val unpackedCount = items.count { !it.isPacked }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PackingHeader()

        PackingSummaryCard(
            packedCount = packedCount,
            unpackedCount = unpackedCount,
            totalCount = items.size
        )

        AddPackingItemCard(
            value = newItemText,
            onValueChange = { newItemText = it },
            onAddClick = {
                val trimmedText = newItemText.trim()

                if (trimmedText.isNotEmpty()) {
                    items.add(
                        PackingItem(
                            id = nextId,
                            name = trimmedText,
                            isPacked = false
                        )
                    )
                    nextId++
                    newItemText = ""
                }
            }
        )

        PackingFilterRow(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
        )

        if (filteredItems.isEmpty()) {
            EmptyPackingState(selectedFilter = selectedFilter)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredItems,
                    key = { it.id }
                ) { item ->
                    PackingItemCard(
                        item = item,
                        onCheckedChange = { isPacked ->
                            val index = items.indexOfFirst { it.id == item.id }
                            if (index != -1) {
                                items[index] = items[index].copy(isPacked = isPacked)
                            }
                        },
                        onDeleteClick = {
                            items.removeAll { it.id == item.id }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PackingHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Travel Packing List",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Add items for a trip, mark them as packed, and filter the list.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PackingSummaryCard(
    packedCount: Int,
    unpackedCount: Int,
    totalCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PackingSummaryValue(
                value = packedCount.toString(),
                label = "Packed",
                modifier = Modifier.weight(1f)
            )
            PackingSummaryValue(
                value = unpackedCount.toString(),
                label = "Left",
                modifier = Modifier.weight(1f)
            )
            PackingSummaryValue(
                value = totalCount.toString(),
                label = "Total",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PackingSummaryValue(
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
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
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
fun AddPackingItemCard(
    value: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Add item",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Packing item") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to list")
            }
        }
    }
}

@Composable
fun PackingFilterRow(
    selectedFilter: PackingFilter,
    onFilterSelected: (PackingFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterButton(
            text = "All",
            selected = selectedFilter == PackingFilter.ALL,
            onClick = { onFilterSelected(PackingFilter.ALL) },
            modifier = Modifier.weight(1f)
        )
        FilterButton(
            text = "Left",
            selected = selectedFilter == PackingFilter.UNPACKED,
            onClick = { onFilterSelected(PackingFilter.UNPACKED) },
            modifier = Modifier.weight(1f)
        )
        FilterButton(
            text = "Packed",
            selected = selectedFilter == PackingFilter.PACKED,
            onClick = { onFilterSelected(PackingFilter.PACKED) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    }
}

@Composable
fun PackingItemCard(
    item: PackingItem,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isPacked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Checkbox(
                checked = item.isPacked,
                onCheckedChange = onCheckedChange
            )

            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (item.isPacked) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )

            OutlinedButton(onClick = onDeleteClick) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun EmptyPackingState(selectedFilter: PackingFilter) {
    val message = when (selectedFilter) {
        PackingFilter.ALL -> "No packing items have been added yet."
        PackingFilter.UNPACKED -> "All visible items are already packed."
        PackingFilter.PACKED -> "No packed items are currently visible."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TravelPackingListScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TravelPackingListScreen()
        }
    }
}