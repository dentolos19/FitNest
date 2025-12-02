package me.dennise.fitnest.ui.states

import me.dennise.fitnest.data.entities.Workout

data class ViewWorkoutUiState(
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false,
    val workout: Workout? = null,
)