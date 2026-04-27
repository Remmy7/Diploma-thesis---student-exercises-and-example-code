package com.example.cv10_ukazka

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY createdAt DESC")
    suspend fun getAllPlants(): List<PlantEntity>

    @Insert
    suspend fun insertPlant(plant: PlantEntity)

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)
}
