package com.example.cv10_ukazka

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val wateringIntervalDays: Int,
    val lastWateredAt: Long,
    val createdAt: Long
)
