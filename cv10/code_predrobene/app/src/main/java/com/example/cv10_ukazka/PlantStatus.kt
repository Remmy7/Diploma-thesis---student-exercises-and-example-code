package com.example.cv10_ukazka

enum class PlantStatus {
    NEEDS_WATER,
    WATER_SOON,
    OK
}

const val MILLIS_IN_DAY: Long = 24L * 60L * 60L * 1000L

fun getPlantStatus(
    plant: PlantEntity,
    currentTime: Long = System.currentTimeMillis()
): PlantStatus {
    val daysSinceWatering = ((currentTime - plant.lastWateredAt) / MILLIS_IN_DAY).toInt()
    val daysUntilWatering = plant.wateringIntervalDays - daysSinceWatering

    return when {
        daysUntilWatering <= 0 -> PlantStatus.NEEDS_WATER
        daysUntilWatering == 1 -> PlantStatus.WATER_SOON
        else -> PlantStatus.OK
    }
}

fun getDaysUntilWatering(
    plant: PlantEntity,
    currentTime: Long = System.currentTimeMillis()
): Int {
    val daysSinceWatering = ((currentTime - plant.lastWateredAt) / MILLIS_IN_DAY).toInt()
    return plant.wateringIntervalDays - daysSinceWatering
}

fun statusPriority(status: PlantStatus): Int {
    return when (status) {
        PlantStatus.NEEDS_WATER -> 0
        PlantStatus.WATER_SOON -> 1
        PlantStatus.OK -> 2
    }
}
