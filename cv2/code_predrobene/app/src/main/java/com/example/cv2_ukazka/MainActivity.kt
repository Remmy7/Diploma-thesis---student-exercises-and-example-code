package com.example.cv2_ukazka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class WorkoutExercise(
    val name: String,
    val targetReps: Int,
    val targetSets: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutRepetitionTrackerScreen()
                }
            }
        }
    }
}

@Composable
fun WorkoutRepetitionTrackerScreen() {
    val exercises = listOf(
        WorkoutExercise(
            name = "Push-ups",
            targetReps = 12,
            targetSets = 3
        ),
        WorkoutExercise(
            name = "Squats",
            targetReps = 15,
            targetSets = 4
        ),
        WorkoutExercise(
            name = "Sit-ups",
            targetReps = 10,
            targetSets = 3
        )
    )

    var selectedExerciseIndex by remember { mutableIntStateOf(0) }
    var currentReps by remember { mutableIntStateOf(0) }
    var completedSets by remember { mutableIntStateOf(0) }
    var totalReps by remember { mutableIntStateOf(0) }

    val selectedExercise = exercises[selectedExerciseIndex]
    val progress = completedSets.toFloat() / selectedExercise.targetSets.toFloat()
    val isExerciseFinished = completedSets >= selectedExercise.targetSets

    fun resetExerciseProgress() {
        currentReps = 0
        completedSets = 0
        totalReps = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderSection()

        ExerciseSelector(
            exercises = exercises,
            selectedExerciseIndex = selectedExerciseIndex,
            onExerciseSelected = { index ->
                selectedExerciseIndex = index
                resetExerciseProgress()
            }
        )

        CurrentExerciseCard(
            exercise = selectedExercise,
            currentReps = currentReps,
            completedSets = completedSets,
            totalReps = totalReps,
            progress = progress.coerceIn(0f, 1f),
            isExerciseFinished = isExerciseFinished
        )

        RepetitionControls(
            currentReps = currentReps,
            onDecreaseClick = {
                if (currentReps > 0) {
                    currentReps--
                }
            },
            onIncreaseClick = {
                if (!isExerciseFinished) {
                    currentReps++
                }
            },
            onAddFiveClick = {
                if (!isExerciseFinished) {
                    currentReps += 5
                }
            }
        )

        SetControls(
            currentReps = currentReps,
            isExerciseFinished = isExerciseFinished,
            onCompleteSetClick = {
                if (currentReps > 0 && !isExerciseFinished) {
                    completedSets++
                    totalReps += currentReps
                    currentReps = 0
                }
            },
            onResetClick = {
                resetExerciseProgress()
            }
        )

        WorkoutSummaryCard(
            exercise = selectedExercise,
            completedSets = completedSets,
            totalReps = totalReps,
            isExerciseFinished = isExerciseFinished
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Workout Repetition Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Track repetitions, complete sets, and see how the screen changes when state values are updated.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ExerciseSelector(
    exercises: List<WorkoutExercise>,
    selectedExerciseIndex: Int,
    onExerciseSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose exercise",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            exercises.forEachIndexed { index, exercise ->
                if (index == selectedExerciseIndex) {
                    Button(
                        onClick = { onExerciseSelected(index) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${exercise.name}  ${exercise.targetSets} x ${exercise.targetReps}")
                    }
                } else {
                    OutlinedButton(
                        onClick = { onExerciseSelected(index) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${exercise.name}  ${exercise.targetSets} x ${exercise.targetReps}")
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentExerciseCard(
    exercise: WorkoutExercise,
    currentReps: Int,
    completedSets: Int,
    totalReps: Int,
    progress: Float,
    isExerciseFinished: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isExerciseFinished) {
                    "Exercise completed"
                } else {
                    "Current set in progress"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WorkoutValueCard(
                    value = currentReps.toString(),
                    label = "Current reps",
                    modifier = Modifier.weight(1f)
                )

                WorkoutValueCard(
                    value = "$completedSets/${exercise.targetSets}",
                    label = "Sets",
                    modifier = Modifier.weight(1f)
                )

                WorkoutValueCard(
                    value = totalReps.toString(),
                    label = "Total reps",
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Set progress",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun WorkoutValueCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun RepetitionControls(
    currentReps: Int,
    onDecreaseClick: () -> Unit,
    onIncreaseClick: () -> Unit,
    onAddFiveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Repetitions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDecreaseClick,
                    enabled = currentReps > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("-1")
                }

                Button(
                    onClick = onIncreaseClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1")
                }

                Button(
                    onClick = onAddFiveClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+5")
                }
            }
        }
    }
}

@Composable
fun SetControls(
    currentReps: Int,
    isExerciseFinished: Boolean,
    onCompleteSetClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Set actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = onCompleteSetClick,
                enabled = currentReps > 0 && !isExerciseFinished,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Complete current set")
            }

            OutlinedButton(
                onClick = onResetClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset exercise")
            }
        }
    }
}

@Composable
fun WorkoutSummaryCard(
    exercise: WorkoutExercise,
    completedSets: Int,
    totalReps: Int,
    isExerciseFinished: Boolean
) {
    val message = when {
        isExerciseFinished -> "Workout finished. You completed all sets for ${exercise.name}."
        completedSets == 0 -> "Start the first set and track your repetitions."
        else -> "Continue with the next set."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Total repetitions recorded: $totalReps",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WorkoutRepetitionTrackerScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WorkoutRepetitionTrackerScreen()
        }
    }
}