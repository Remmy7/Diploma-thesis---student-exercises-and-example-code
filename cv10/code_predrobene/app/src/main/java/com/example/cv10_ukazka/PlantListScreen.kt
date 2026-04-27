package com.example.cv10_ukazka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlantListScreen(
    uiState: PlantUiState,
    onAddPlantClick: () -> Unit,
    onWaterPlantClick: (PlantEntity) -> Unit,
    onDeletePlantClick: (PlantEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PlantHeaderCard(onAddPlantClick = onAddPlantClick)
        }

        item {
            PlantLegendCard()
        }

        if (uiState.plants.isEmpty()) {
            item {
                EmptyPlantsCard()
            }
        } else {
            items(
                items = uiState.plants,
                key = { it.id }
            ) { plant ->
                PlantCard(
                    plant = plant,
                    onWaterPlantClick = { onWaterPlantClick(plant) },
                    onDeletePlantClick = { onDeletePlantClick(plant) }
                )
            }
        }
    }
}

@Composable
fun PlantHeaderCard(
    onAddPlantClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Plant Watering Log",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Track plants, watering intervals, and the current watering status.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onAddPlantClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add plant")
            }
        }
    }
}

@Composable
fun PlantLegendCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Status order",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Plants are sorted by urgency: red, yellow, then green.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun PlantCard(
    plant: PlantEntity,
    onWaterPlantClick: () -> Unit,
    onDeletePlantClick: () -> Unit
) {
    val status = getPlantStatus(plant)
    val daysUntilWatering = getDaysUntilWatering(plant)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = plantStatusContainerColor(status)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlantStatusDot(status = status)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Water every ${plant.wateringIntervalDays} day(s)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Text(
                text = plantStatusText(daysUntilWatering),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onWaterPlantClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Water now")
                }

                OutlinedButton(
                    onClick = onDeletePlantClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun PlantStatusDot(status: PlantStatus) {
    Box(
        modifier = Modifier
            .background(
                color = plantStatusStrongColor(status),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = when (status) {
                PlantStatus.NEEDS_WATER -> "RED"
                PlantStatus.WATER_SOON -> "YELLOW"
                PlantStatus.OK -> "GREEN"
            },
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyPlantsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "No plants have been added yet.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun plantStatusText(daysUntilWatering: Int): String {
    return when {
        daysUntilWatering <= 0 -> "Water today"
        daysUntilWatering == 1 -> "Water tomorrow"
        else -> "OK for $daysUntilWatering more days"
    }
}

fun plantStatusContainerColor(status: PlantStatus): Color {
    return when (status) {
        PlantStatus.NEEDS_WATER -> Color(0xFFFFDAD6)
        PlantStatus.WATER_SOON -> Color(0xFFFFF3C4)
        PlantStatus.OK -> Color(0xFFD8F5D0)
    }
}

fun plantStatusStrongColor(status: PlantStatus): Color {
    return when (status) {
        PlantStatus.NEEDS_WATER -> Color(0xFFB3261E)
        PlantStatus.WATER_SOON -> Color(0xFFB26A00)
        PlantStatus.OK -> Color(0xFF2E7D32)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlantListScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PlantListScreen(
                uiState = PlantUiState(
                    plants = listOf(
                        PlantEntity(
                            id = 1,
                            name = "Monstera",
                            wateringIntervalDays = 7,
                            lastWateredAt = System.currentTimeMillis() - 7 * MILLIS_IN_DAY,
                            createdAt = System.currentTimeMillis()
                        ),
                        PlantEntity(
                            id = 2,
                            name = "Pothos",
                            wateringIntervalDays = 5,
                            lastWateredAt = System.currentTimeMillis() - 4 * MILLIS_IN_DAY,
                            createdAt = System.currentTimeMillis()
                        ),
                        PlantEntity(
                            id = 3,
                            name = "Snake Plant",
                            wateringIntervalDays = 14,
                            lastWateredAt = System.currentTimeMillis(),
                            createdAt = System.currentTimeMillis()
                        )
                    )
                ),
                onAddPlantClick = {},
                onWaterPlantClick = {},
                onDeletePlantClick = {}
            )
        }
    }
}
