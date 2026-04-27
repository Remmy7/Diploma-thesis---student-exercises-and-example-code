package com.example.cv9_ukazka

data class JournalUiState(
    val titleInput: String = "",
    val textInput: String = "",
    val selectedMood: JournalMood = JournalMood.CALM,
    val entries: List<JournalEntryEntity> = emptyList(),
    val errorMessage: String? = null
)
