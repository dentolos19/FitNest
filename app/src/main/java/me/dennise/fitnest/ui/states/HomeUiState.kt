package me.dennise.fitnest.ui.states

import me.dennise.fitnest.data.entities.Workout

data class HomeUiState(
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = false,
)