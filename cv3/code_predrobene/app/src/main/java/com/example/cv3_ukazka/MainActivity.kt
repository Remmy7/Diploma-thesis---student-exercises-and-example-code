package com.example.cv3_ukazka

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class FeedbackTopic {
    APP_DESIGN,
    PERFORMANCE,
    CONTENT
}

data class FeedbackFormState(
    val name: String = "",
    val email: String = "",
    val message: String = "",
    val topic: FeedbackTopic = FeedbackTopic.APP_DESIGN,
    val rating: Float = 3f,
    val wantsReply: Boolean = false,
    val acceptedTerms: Boolean = false,
    val submitted: Boolean = false
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
                    FeedbackFormScreen()
                }
            }
        }
    }
}

@Composable
fun FeedbackFormScreen() {
    var formState by remember { mutableStateOf(FeedbackFormState()) }

    val nameError = formState.name.isBlank()
    val emailError = formState.email.isBlank() || !formState.email.contains("@")
    val messageError = formState.message.length < 10
    val termsError = !formState.acceptedTerms
    val isFormValid = !nameError && !emailError && !messageError && !termsError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderSection()

        FeedbackCard(
            formState = formState,
            nameError = nameError,
            emailError = emailError,
            messageError = messageError,
            termsError = termsError,
            isFormValid = isFormValid,
            onNameChange = { formState = formState.copy(name = it, submitted = false) },
            onEmailChange = { formState = formState.copy(email = it, submitted = false) },
            onMessageChange = { formState = formState.copy(message = it, submitted = false) },
            onTopicChange = { formState = formState.copy(topic = it, submitted = false) },
            onRatingChange = { formState = formState.copy(rating = it, submitted = false) },
            onWantsReplyChange = { formState = formState.copy(wantsReply = it, submitted = false) },
            onAcceptedTermsChange = { formState = formState.copy(acceptedTerms = it, submitted = false) },
            onSubmitClick = { formState = formState.copy(submitted = true) },
            onResetClick = { formState = FeedbackFormState() }
        )

        if (formState.submitted) {
            SubmittedFeedbackCard(formState = formState)
        }
    }
}

@Composable
fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Feedback Form",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Fill in a short feedback form and see how validation reacts to the entered values.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FeedbackCard(
    formState: FeedbackFormState,
    nameError: Boolean,
    emailError: Boolean,
    messageError: Boolean,
    termsError: Boolean,
    isFormValid: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onTopicChange: (FeedbackTopic) -> Unit,
    onRatingChange: (Float) -> Unit,
    onWantsReplyChange: (Boolean) -> Unit,
    onAcceptedTermsChange: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    onResetClick: () -> Unit
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
            FormTextField(
                value = formState.name,
                onValueChange = onNameChange,
                label = "Name",
                isError = nameError,
                errorMessage = "Name must not be empty."
            )

            FormTextField(
                value = formState.email,
                onValueChange = onEmailChange,
                label = "Email",
                isError = emailError,
                errorMessage = "Email must contain @."
            )

            TopicSelector(
                selectedTopic = formState.topic,
                onTopicChange = onTopicChange
            )

            RatingSection(
                rating = formState.rating,
                onRatingChange = onRatingChange
            )

            FormTextField(
                value = formState.message,
                onValueChange = onMessageChange,
                label = "Message",
                isError = messageError,
                errorMessage = "Message must have at least 10 characters.",
                minLines = 3
            )

            ReplySwitchRow(
                checked = formState.wantsReply,
                onCheckedChange = onWantsReplyChange
            )

            TermsRow(
                checked = formState.acceptedTerms,
                isError = termsError,
                onCheckedChange = onAcceptedTermsChange
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onSubmitClick,
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }

                OutlinedButton(
                    onClick = onResetClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
            }
        }
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            minLines = minLines
        )

        if (isError) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun TopicSelector(
    selectedTopic: FeedbackTopic,
    onTopicChange: (FeedbackTopic) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Topic",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TopicButton(
                text = "Design",
                selected = selectedTopic == FeedbackTopic.APP_DESIGN,
                onClick = { onTopicChange(FeedbackTopic.APP_DESIGN) },
                modifier = Modifier.weight(1f)
            )
            TopicButton(
                text = "Speed",
                selected = selectedTopic == FeedbackTopic.PERFORMANCE,
                onClick = { onTopicChange(FeedbackTopic.PERFORMANCE) },
                modifier = Modifier.weight(1f)
            )
            TopicButton(
                text = "Content",
                selected = selectedTopic == FeedbackTopic.CONTENT,
                onClick = { onTopicChange(FeedbackTopic.CONTENT) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TopicButton(
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
fun RatingSection(
    rating: Float,
    onRatingChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Rating: ${rating.toInt()}/5",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Slider(
            value = rating,
            onValueChange = onRatingChange,
            valueRange = 1f..5f,
            steps = 3
        )
    }
}

@Composable
fun ReplySwitchRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "I would like to receive a reply",
            style = MaterialTheme.typography.bodyMedium
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun TermsRow(
    checked: Boolean,
    isError: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            Text(
                text = "I agree with sending this feedback.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isError) {
            Text(
                text = "You must agree before submitting.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun SubmittedFeedbackCard(formState: FeedbackFormState) {
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
                text = "Feedback submitted",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Thank you, ${formState.name}. Your feedback was recorded locally on this screen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedbackFormScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            FeedbackFormScreen()
        }
    }
}