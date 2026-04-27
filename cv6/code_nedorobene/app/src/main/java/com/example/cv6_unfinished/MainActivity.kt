package com.example.cv6_unfinished

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class StudyHubUiState {
    LOADING,
    SUCCESS,
    EMPTY,
    ERROR
}

data class StudyModule(
    val title: String,
    val subtitle: String,
    val buttonText: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyHubBeforeScreen()
                }
            }
        }
    }
}

@Composable
fun StudyHubBeforeScreen() {
    var selectedState by remember { mutableStateOf(StudyHubUiState.SUCCESS) }

    val modules = listOf(
        StudyModule(
            title = "Compose Basics",
            subtitle = "5 lessons available",
            buttonText = "Open"
        ),
        StudyModule(
            title = "State and Recomposition",
            subtitle = "2 practice tasks",
            buttonText = "Continue"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Study Hub")

        Text(text = "Course materials overview")

        StateButtons(
            selectedState = selectedState,
            onStateSelected = { selectedState = it }
        )

        when (selectedState) {
            StudyHubUiState.LOADING -> LoadingContent()
            StudyHubUiState.SUCCESS -> SuccessContent(modules = modules)
            StudyHubUiState.EMPTY -> EmptyContent()
            StudyHubUiState.ERROR -> ErrorContent(
                onRetry = { selectedState = StudyHubUiState.SUCCESS }
            )
        }
    }
}

@Composable
fun StateButtons(
    selectedState: StudyHubUiState,
    onStateSelected: (StudyHubUiState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Preview state")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onStateSelected(StudyHubUiState.LOADING) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Loading")
            }

            Button(
                onClick = { onStateSelected(StudyHubUiState.SUCCESS) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Success")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onStateSelected(StudyHubUiState.EMPTY) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Empty")
            }

            Button(
                onClick = { onStateSelected(StudyHubUiState.ERROR) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Error")
            }
        }
    }
}

@Composable
fun SuccessContent(modules: List<StudyModule>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Available modules")

        modules.forEach { module ->
            ModuleItem(module = module)
        }
    }
}

@Composable
fun ModuleItem(module: StudyModule) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = module.title)
        Text(text = module.subtitle)

        Button(onClick = {}) {
            Text(module.buttonText)
        }
    }
}

@Composable
fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircularProgressIndicator()

        Text(text = "Loading study materials...")
        Text(text = "Please wait while the content is prepared.")
    }
}

@Composable
fun EmptyContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "No study materials yet")
        Text(text = "There is currently no content to display.")

        Button(onClick = {}) {
            Text("Refresh")
        }
    }
}

@Composable
fun ErrorContent(
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Unable to load materials")
        Text(text = "Something went wrong while loading the content.")

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudyHubBeforePreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StudyHubBeforeScreen()
        }
    }
}