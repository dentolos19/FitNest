package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.data.EnjoymentRating
import me.dennise.fitnest.data.entities.Workout
import me.dennise.fitnest.getWorkoutCategoryIcon
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.components.LabelValue
import me.dennise.fitnest.ui.models.ViewWorkoutViewModel
import me.dennise.fitnest.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewWorkoutScreen(
    workoutId: Int,
    onNavigateBack: () -> Unit = {},
    viewModel: ViewWorkoutViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    // Load workout when screen is displayed
    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    // Navigate back when workout is deleted
    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Workout?") },
            text = { Text("Are you sure you want to delete this workout? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteWorkout()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = uiState.workout?.category ?: "Workout Details",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            // Loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.workout != null) {
            // Display workout details
            WorkoutDetailContent(
                workout = uiState.workout!!,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            // Error state - workout not found
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Workout not found",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Go Back")
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutDetailContent(
    workout: Workout,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Workout Details Card with Icon
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Workout Icon (centered vertically)
                Icon(
                    imageVector = getWorkoutCategoryIcon(workout.category),
                    contentDescription = workout.category,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                // Workout Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LabelValue(
                        label = "Category",
                        value = workout.category
                    )
                    LabelValue(
                        label = "Date",
                        value = workout.date ?: "Not specified"
                    )
                    LabelValue(
                        label = "Time",
                        value = workout.time ?: "Not specified"
                    )
                    LabelValue(
                        label = "Duration",
                        value = if (workout.duration != null) "${workout.duration} minutes" else "Not specified"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Details Section - Full Width
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Rating Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                val enjoymentRating = EnjoymentRating.entries.find {
                    it.label.equals(workout.enjoyment, ignoreCase = true)
                }

                LabelValue(
                    label = "Enjoyment",
                    value = if (enjoymentRating != null) "${enjoymentRating.emoji} ${workout.enjoyment}" else workout.enjoyment
                )

                Spacer(modifier = Modifier.height(4.dp))

                LabelValue(
                    label = "Comments",
                    value = workout.comments ?: "No comments"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun ViewWorkoutPreview() {
    AppTheme {
        WorkoutDetailContent(
            workout = Workout(
                id = 1,
                userId = 1,
                name = "Morning Run",
                category = "Cardio",
                duration = 30,
                date = "Nov 13, 2025",
                time = "07:00 AM",
                comments = "Great workout! Felt energized after.",
                enjoyment = "Energizing"
            )
        )
    }
}