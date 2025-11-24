package me.dennise.fitnest.ui.states

import me.dennise.fitnest.data.EnjoymentRating
import me.dennise.fitnest.data.WorkoutCategory

data class WorkoutAddState(
    val name: String = "",
    val nameError: String? = null,
    val category: String = WorkoutCategory.CARDIO.displayName,
    val duration: String = "",
    val comments: String = "",
    val enjoyment: String = EnjoymentRating.ENERGIZING.displayName,
    val enjoymentIndex: Int = 0,
    val date: String = "",
    val time: String = ""
)