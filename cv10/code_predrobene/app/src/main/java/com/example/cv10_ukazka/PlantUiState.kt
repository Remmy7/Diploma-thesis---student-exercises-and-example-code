package com.example.cv10_ukazka

data class PlantUiState(
    val nameInput: String = "",
    val intervalInput: String = "",
    val plants: List<PlantEntity> = emptyList(),
    val errorMessage: String? = null
)
