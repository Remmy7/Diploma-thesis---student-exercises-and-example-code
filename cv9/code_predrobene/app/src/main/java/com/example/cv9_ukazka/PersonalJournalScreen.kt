package com.example.cv9_ukazka

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PersonalJournalScreen(
    uiState: JournalUiState,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onMoodSelected: (JournalMood) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: (JournalEntryEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            JournalHeader()
        }

        item {
            AddJournalEntryCard(
                titleInput = uiState.titleInput,
                textInput = uiState.textInput,
                selectedMood = uiState.selectedMood,
                onTitleChange = onTitleChange,
                onTextChange = onTextChange,
                onMoodSelected = onMoodSelected,
                onSaveClick = onSaveClick
            )
        }

        uiState.errorMessage?.let { message ->
            item {
                ErrorCard(message = message)
            }
        }

        item {
            JournalSummaryCard(entries = uiState.entries)
        }

        if (uiState.entries.isEmpty()) {
            item {
                EmptyJournalCard()
            }
        } else {
            items(
                items = uiState.entries,
                key = { it.id }
            ) { entry ->
                JournalEntryCard(
                    entry = entry,
                    onDeleteClick = { onDeleteClick(entry) }
                )
            }
        }
    }
}

@Composable
fun JournalHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Personal Journal Lite",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Write short journal entries and keep them stored locally with Room.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AddJournalEntryCard(
    titleInput: String,
    textInput: String,
    selectedMood: JournalMood,
    onTitleChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onMoodSelected: (JournalMood) -> Unit,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "New entry",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = titleInput,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            OutlinedTextField(
                value = textInput,
                onValueChange = onTextChange,
                label = { Text("Journal text") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            MoodSelector(
                selectedMood = selectedMood,
                onMoodSelected = onMoodSelected
            )

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save entry")
            }
        }
    }
}

@Composable
fun MoodSelector(
    selectedMood: JournalMood,
    onMoodSelected: (JournalMood) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Mood",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MoodButton(
                text = "Happy",
                selected = selectedMood == JournalMood.HAPPY,
                onClick = { onMoodSelected(JournalMood.HAPPY) },
                modifier = Modifier.weight(1f)
            )

            MoodButton(
                text = "Calm",
                selected = selectedMood == JournalMood.CALM,
                onClick = { onMoodSelected(JournalMood.CALM) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MoodButton(
                text = "Tired",
                selected = selectedMood == JournalMood.TIRED,
                onClick = { onMoodSelected(JournalMood.TIRED) },
                modifier = Modifier.weight(1f)
            )

            MoodButton(
                text = "Stressed",
                selected = selectedMood == JournalMood.STRESSED,
                onClick = { onMoodSelected(JournalMood.STRESSED) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MoodButton(
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
fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun JournalSummaryCard(entries: List<JournalEntryEntity>) {
    val mostRecentMood = entries.firstOrNull()?.mood?.name?.lowercase()?.replaceFirstChar {
        it.uppercase()
    } ?: "-"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Journal summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Entries saved: ${entries.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Most recent mood: $mostRecentMood",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun EmptyJournalCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "No journal entries have been saved yet.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun JournalEntryCard(
    entry: JournalEntryEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${formatMood(entry.mood)} • ${formatTimestamp(entry.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = entry.text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            OutlinedButton(
                onClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete")
            }
        }
    }
}

private fun formatMood(mood: JournalMood): String {
    return mood.name.lowercase().replaceFirstChar { it.uppercase() }
}

private fun formatTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PersonalJournalScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PersonalJournalScreen(
                uiState = JournalUiState(
                    titleInput = "",
                    textInput = "",
                    selectedMood = JournalMood.CALM,
                    entries = listOf(
                        JournalEntryEntity(
                            id = 1,
                            title = "Quiet evening",
                            text = "Today I finished several tasks and spent the evening reading.",
                            mood = JournalMood.CALM,
                            createdAt = 1743490800000
                        ),
                        JournalEntryEntity(
                            id = 2,
                            title = "Long day",
                            text = "The day was busy, but I managed to complete the most important work.",
                            mood = JournalMood.TIRED,
                            createdAt = 1743487200000
                        )
                    )
                ),
                onTitleChange = {},
                onTextChange = {},
                onMoodSelected = {},
                onSaveClick = {},
                onDeleteClick = {}
            )
        }
    }
}
