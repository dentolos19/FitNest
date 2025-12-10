package me.dennise.fitnest

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.ui.graphics.vector.ImageVector
import me.dennise.fitnest.data.types.WorkoutCategory

fun getWorkoutCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        WorkoutCategory.CARDIO.label.lowercase() -> Icons.AutoMirrored.Filled.DirectionsRun
        WorkoutCategory.STRENGTH.label.lowercase() -> Icons.Default.FitnessCenter
        WorkoutCategory.FLEXIBILITY_MOBILITY.label.lowercase() -> Icons.Default.SelfImprovement
        WorkoutCategory.MIND_BODY_RECOVERY.label.lowercase() -> Icons.Default.SelfImprovement
        else -> Icons.Default.FitnessCenter
    }
}