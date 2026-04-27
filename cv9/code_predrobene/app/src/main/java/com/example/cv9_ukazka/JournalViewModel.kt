package com.example.cv9_ukazka

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JournalRepository

    var uiState by mutableStateOf(JournalUiState())
        private set

    init {
        val dao = JournalDatabase.getDatabase(application).journalEntryDao()
        repository = JournalRepository(dao)
        loadEntries()
    }

    fun onTitleChange(newValue: String) {
        uiState = uiState.copy(
            titleInput = newValue,
            errorMessage = null
        )
    }

    fun onTextChange(newValue: String) {
        uiState = uiState.copy(
            textInput = newValue,
            errorMessage = null
        )
    }

    fun onMoodSelected(mood: JournalMood) {
        uiState = uiState.copy(selectedMood = mood)
    }

    fun addEntry() {
        val title = uiState.titleInput.trim()
        val text = uiState.textInput.trim()

        if (title.isBlank()) {
            uiState = uiState.copy(errorMessage = "Please enter a title.")
            return
        }

        if (text.length < 10) {
            uiState = uiState.copy(errorMessage = "Journal text must have at least 10 characters.")
            return
        }

        viewModelScope.launch {
            repository.insertEntry(
                JournalEntryEntity(
                    title = title,
                    text = text,
                    mood = uiState.selectedMood,
                    createdAt = System.currentTimeMillis()
                )
            )

            uiState = uiState.copy(
                titleInput = "",
                textInput = "",
                selectedMood = JournalMood.CALM,
                errorMessage = null
            )

            loadEntries()
        }
    }

    fun deleteEntry(entry: JournalEntryEntity) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            loadEntries()
        }
    }

    private fun loadEntries() {
        viewModelScope.launch {
            val entries = repository.getAllEntries()
            uiState = uiState.copy(entries = entries)
        }
    }
}
