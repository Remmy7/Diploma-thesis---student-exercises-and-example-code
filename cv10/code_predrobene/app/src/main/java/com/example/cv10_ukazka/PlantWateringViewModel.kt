package com.example.cv10_ukazka

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantWateringViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    private val _uiState = MutableStateFlow(PlantUiState())
    val uiState: StateFlow<PlantUiState> = _uiState

    init {
        val dao = PlantDatabase.getDatabase(application).plantDao()
        repository = PlantRepository(dao)
        loadPlants()
    }

    fun onNameChange(newValue: String) {
        _uiState.update {
            it.copy(
                nameInput = newValue,
                errorMessage = null
            )
        }
    }

    fun onIntervalChange(newValue: String) {
        val cleanValue = newValue.filter { char -> char.isDigit() }.take(3)

        _uiState.update {
            it.copy(
                intervalInput = cleanValue,
                errorMessage = null
            )
        }
    }

    fun addPlant(): Boolean {
        val currentState = _uiState.value
        val name = currentState.nameInput.trim()
        val interval = currentState.intervalInput.toIntOrNull()

        if (name.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter a plant name.")
            }
            return false
        }

        if (interval == null || interval < 1) {
            _uiState.update {
                it.copy(errorMessage = "Watering interval must be at least 1 day.")
            }
            return false
        }

        val now = System.currentTimeMillis()

        viewModelScope.launch {
            repository.insertPlant(
                PlantEntity(
                    name = name,
                    wateringIntervalDays = interval,
                    lastWateredAt = now,
                    createdAt = now
                )
            )

            _uiState.update {
                it.copy(
                    nameInput = "",
                    intervalInput = "",
                    errorMessage = null
                )
            }

            loadPlants()
        }

        return true
    }

    fun waterPlant(plant: PlantEntity) {
        viewModelScope.launch {
            repository.updatePlant(
                plant.copy(lastWateredAt = System.currentTimeMillis())
            )

            loadPlants()
        }
    }

    fun deletePlant(plant: PlantEntity) {
        viewModelScope.launch {
            repository.deletePlant(plant)
            loadPlants()
        }
    }

    private fun loadPlants() {
        viewModelScope.launch {
            val sortedPlants = repository.getAllPlants().sortedWith(
                compareBy<PlantEntity> { statusPriority(getPlantStatus(it)) }
                    .thenBy { getDaysUntilWatering(it) }
                    .thenBy { it.name.lowercase() }
            )

            _uiState.update {
                it.copy(plants = sortedPlants)
            }
        }
    }
}
