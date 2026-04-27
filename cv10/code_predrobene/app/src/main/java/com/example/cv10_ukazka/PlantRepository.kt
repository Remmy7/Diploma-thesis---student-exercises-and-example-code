package com.example.cv10_ukazka

class PlantRepository(
    private val plantDao: PlantDao
) {
    suspend fun getAllPlants(): List<PlantEntity> {
        return plantDao.getAllPlants()
    }

    suspend fun insertPlant(plant: PlantEntity) {
        plantDao.insertPlant(plant)
    }

    suspend fun updatePlant(plant: PlantEntity) {
        plantDao.updatePlant(plant)
    }

    suspend fun deletePlant(plant: PlantEntity) {
        plantDao.deletePlant(plant)
    }
}
