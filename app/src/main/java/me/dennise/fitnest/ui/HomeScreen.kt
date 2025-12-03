@file:OptIn(ExperimentalMaterial3Api::class)

package me.dennise.fitnest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import me.dennise.fitnest.Session
import me.dennise.fitnest.data.entities.Workout
import me.dennise.fitnest.getWorkoutCategoryIcon
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.models.HomeViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onViewProfile: () -> Unit = {},
    onAddWorkout: () -> Unit = {},
    onWorkoutClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadWorkouts()
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "FitNest",
                actions = {
                    IconButton(onClick = onAddWorkout) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Workout"
                        )
                    }
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
                                text = { Text("View Profile") },
                                onClick = {
                                    showMenu = false
                                    onViewProfile()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    showMenu = false
                                    scope.launch {
                                        Session.logout()
                                        onLogout()
                                    }
                                }
                            )
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        if (uiState.workouts.isEmpty()) {
            EmptyScreen(
                modifier = Modifier.padding(innerPadding),
                onAddSampleData = { viewModel.addSampleWorkouts() }
            )
        } else {
            WorkoutsScreen(
                workouts = uiState.workouts,
                onWorkoutClick = onWorkoutClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    onAddSampleData: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No entries made",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the + icon to add your first workout",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onAddSampleData) {
                Text("Add Sample Workouts (For Testing)")
            }
        }
    }
}

@Composable
fun WorkoutsScreen(
    workouts: List<Workout>,
    onWorkoutClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(workouts) { workout ->
            WorkoutItem(
                workout = workout,
                onClick = { onWorkoutClick(workout.id) }
            )
        }
    }
}

@Composable
fun WorkoutItem(
    workout: Workout,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Workout type icon
            Icon(
                imageVector = getWorkoutCategoryIcon(workout.category),
                contentDescription = workout.category,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Workout details, date, duration
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = workout.date ?: "No date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${workout.duration ?: 0} minutes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Rating
            Text(
                text = workout.enjoyment,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}