package com.example.cv10_ukazka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
                    val navController = rememberNavController()
                    val viewModel: PlantWateringViewModel = viewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    NavHost(
                        navController = navController,
                        startDestination = "plants"
                    ) {
                        composable("plants") {
                            PlantListScreen(
                                uiState = uiState,
                                onAddPlantClick = {
                                    navController.navigate("addPlant")
                                },
                                onWaterPlantClick = viewModel::waterPlant,
                                onDeletePlantClick = viewModel::deletePlant
                            )
                        }

                        composable("addPlant") {
                            AddPlantScreen(
                                uiState = uiState,
                                onNameChange = viewModel::onNameChange,
                                onIntervalChange = viewModel::onIntervalChange,
                                onSaveClick = {
                                    val saved = viewModel.addPlant()
                                    if (saved) {
                                        navController.popBackStack()
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
