package me.dennise.fitnest.ui.states

import me.dennise.fitnest.data.WorkoutCategory
import me.dennise.fitnest.data.WorkoutEnjoyment

data class AddWorkoutUiState(
    val name: String = "",
    val nameError: String? = null,
    val category: String = WorkoutCategory.CARDIO.label,
    val duration: String = "",
    val comments: String = "",
    val enjoyment: String = WorkoutEnjoyment.ENERGIZING.label,
    val enjoymentIndex: Int = 0,
    val date: String = "",
    val time: String = ""
)