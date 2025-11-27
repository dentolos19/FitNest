package me.dennise.fitnest.ui.states

import me.dennise.fitnest.data.entities.Workout

data class WorkoutDetailUiState(
    val workout: Workout? = null,
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)