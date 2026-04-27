package com.example.cv7_ukazka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Pet(
    val id: Int,
    val name: String,
    val type: String,
    val age: String,
    val personality: String,
    val description: String
)

val samplePets = listOf(
    Pet(
        id = 1,
        name = "Yummi",
        type = "Cat",
        age = "2 years",
        personality = "Calm",
        description = "Yummi likes quiet places, soft blankets, and patient people."
    ),
    Pet(
        id = 2,
        name = "Zdena",
        type = "Dog",
        age = "4 years",
        personality = "Playful",
        description = "Zdena enjoys walks, toys, and active families."
    ),
    Pet(
        id = 3,
        name = "Nora",
        type = "Cat",
        age = "3 years",
        personality = "Hunter",
        description = "Nora likes hunting birds."
    ),
    Pet(
        id = 4,
        name = "Meowster",
        type = "Cat",
        age = "5 years",
        personality = "Independent",
        description = "Meowster prefers calm homes and a comfortable place near a window."
    )
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
                    PetAdoptionApp()
                }
            }
        }
    }
}

@Composable
fun PetAdoptionApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pets"
    ) {
        composable("pets") {
            PetListScreen(
                pets = samplePets,
                onOpenPet = { petId ->
                    navController.navigate("petDetail/$petId")
                }
            )
        }

        composable(
            route = "petDetail/{petId}",
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val pet = samplePets.firstOrNull { it.id == petId }

            if (pet == null) {
                MissingPetScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                PetDetailScreen(
                    pet = pet,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContactClick = {
                        navController.navigate("contact/${pet.id}")
                    }
                )
            }
        }

        composable(
            route = "contact/{petId}",
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val pet = samplePets.firstOrNull { it.id == petId }

            if (pet == null) {
                MissingPetScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            } else {
                ContactShelterScreen(
                    pet = pet,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFinishClick = {
                        navController.popBackStack(
                            route = "pets",
                            inclusive = false
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun PetListScreen(
    pets: List<Pet>,
    onOpenPet: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AppHeader()
        }

        items(
            items = pets,
            key = { it.id }
        ) { pet ->
            PetCard(
                pet = pet,
                onOpenClick = {
                    onOpenPet(pet.id)
                }
            )
        }
    }
}

@Composable
fun AppHeader() {
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
                text = "Pet Adoption",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Choose a pet, open its detail, and continue to the shelter contact screen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    onOpenClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PetAvatar(label = pet.name.take(1))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${pet.type} • ${pet.age}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = pet.personality,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = onOpenClick) {
                Text("Open")
            }
        }
    }
}

@Composable
fun PetAvatar(label: String) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun PetDetailScreen(
    pet: Pet,
    onBackClick: () -> Unit,
    onContactClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(onClick = onBackClick) {
            Text("Back")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PetAvatar(label = pet.name.take(1))

                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${pet.type} • ${pet.age} • ${pet.personality}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = pet.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onContactClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Contact shelter")
                }
            }
        }
    }
}

@Composable
fun ContactShelterScreen(
    pet: Pet,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(onClick = onBackClick) {
            Text("Back")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Contact shelter",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "You are asking about ${pet.name}.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Shelter email: adoption@example.com",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Phone: +421 900 000 000",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onFinishClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return to pets")
                }
            }
        }
    }
}

@Composable
fun MissingPetScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pet not found",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetAdoptionAppPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PetAdoptionApp()
        }
    }
}
