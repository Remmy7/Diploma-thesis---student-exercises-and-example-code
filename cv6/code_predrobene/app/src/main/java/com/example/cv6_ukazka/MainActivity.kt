package com.example.cv6_ukazka

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LibraryScreenState {
    LOADING,
    SUCCESS,
    EMPTY,
    ERROR
}

data class BorrowedBook(
    val title: String,
    val author: String,
    val dueDate: String,
    val progress: Float
)

data class LibraryNotice(
    val title: String,
    val text: String
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
                    LibraryStatusScreen()
                }
            }
        }
    }
}

@Composable
fun LibraryStatusScreen() {
    var selectedState by remember { mutableStateOf(LibraryScreenState.SUCCESS) }

    val borrowedBooks = listOf(
        BorrowedBook(
            title = "Clean Code",
            author = "Robert C. Martin",
            dueDate = "12 May",
            progress = 0.65f
        ),
        BorrowedBook(
            title = "Android Basics with Compose",
            author = "Google Developers",
            dueDate = "18 May",
            progress = 0.40f
        ),
        BorrowedBook(
            title = "Design of Everyday Things",
            author = "Don Norman",
            dueDate = "25 May",
            progress = 0.82f
        )
    )

    val notices = listOf(
        LibraryNotice(
            title = "Reservation ready",
            text = "One reserved book is ready for pickup."
        ),
        LibraryNotice(
            title = "Reading room",
            text = "The reading room is open until 18:00 today."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LibraryHeader()

        StateSelector(
            selectedState = selectedState,
            onStateSelected = { selectedState = it }
        )

        when (selectedState) {
            LibraryScreenState.LOADING -> LoadingStateCard()
            LibraryScreenState.SUCCESS -> LibrarySuccessContent(
                borrowedBooks = borrowedBooks,
                notices = notices
            )
            LibraryScreenState.EMPTY -> EmptyLibraryState()
            LibraryScreenState.ERROR -> ErrorLibraryState(
                onRetryClick = { selectedState = LibraryScreenState.SUCCESS }
            )
        }
    }
}

@Composable
fun LibraryHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Library Status",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Check borrowed books, reading progress, and current library notices.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StateSelector(
    selectedState: LibraryScreenState,
    onStateSelected: (LibraryScreenState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Screen state",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StateButton(
                    text = "Loading",
                    selected = selectedState == LibraryScreenState.LOADING,
                    onClick = { onStateSelected(LibraryScreenState.LOADING) },
                    modifier = Modifier.weight(1f)
                )

                StateButton(
                    text = "Success",
                    selected = selectedState == LibraryScreenState.SUCCESS,
                    onClick = { onStateSelected(LibraryScreenState.SUCCESS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StateButton(
                    text = "Empty",
                    selected = selectedState == LibraryScreenState.EMPTY,
                    onClick = { onStateSelected(LibraryScreenState.EMPTY) },
                    modifier = Modifier.weight(1f)
                )

                StateButton(
                    text = "Error",
                    selected = selectedState == LibraryScreenState.ERROR,
                    onClick = { onStateSelected(LibraryScreenState.ERROR) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StateButton(
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
fun LibrarySuccessContent(
    borrowedBooks: List<BorrowedBook>,
    notices: List<LibraryNotice>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LibrarySummaryCard(
            borrowedCount = borrowedBooks.size,
            noticeCount = notices.size
        )

        SectionCard(title = "Borrowed books") {
            borrowedBooks.forEach { book ->
                BorrowedBookCard(book = book)
            }
        }

        SectionCard(title = "Library notices") {
            notices.forEach { notice ->
                NoticeCard(notice = notice)
            }
        }
    }
}

@Composable
fun LibrarySummaryCard(
    borrowedCount: Int,
    noticeCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryValue(
                value = borrowedCount.toString(),
                label = "Borrowed",
                modifier = Modifier.weight(1f)
            )

            SummaryValue(
                value = noticeCount.toString(),
                label = "Notices",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SummaryValue(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun BorrowedBookCard(book: BorrowedBook) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Due date: ${book.dueDate}",
                style = MaterialTheme.typography.bodySmall
            )

            LinearProgressIndicator(
                progress = { book.progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NoticeCard(notice: LibraryNotice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = notice.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = notice.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            content()
        }
    }
}

@Composable
fun LoadingStateCard() {
    StatusMessageCard {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Loading library data...",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "The screen is waiting for book and notice information.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyLibraryState() {
    StatusMessageCard {
        Text(
            text = "No borrowed books",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "There are currently no borrowed books or active library notices.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorLibraryState(
    onRetryClick: () -> Unit
) {
    StatusMessageCard {
        Text(
            text = "Unable to load library status",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The screen can show an error state and offer a simple retry action.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}

@Composable
fun StatusMessageCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryStatusScreenPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LibraryStatusScreen()
        }
    }
}
