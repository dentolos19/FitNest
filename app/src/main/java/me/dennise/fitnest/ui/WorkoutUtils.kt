package me.dennise.fitnest.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Returns the appropriate icon for a given workout category.
 * Supports both the original category names and the new standardized ones.
 */
fun getWorkoutCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        // Original categories from landing page
        "running" -> Icons.AutoMirrored.Filled.DirectionsRun
        "cycling" -> Icons.AutoMirrored.Filled.DirectionsBike
        "swimming" -> Icons.Default.Pool
        "yoga" -> Icons.Default.SelfImprovement
        "sports" -> Icons.Default.SportsBasketball
        "gym" -> Icons.Default.FitnessCenter

        // New standardized categories from Add Workout
        WorkoutCategory.CARDIO.displayName.lowercase() -> Icons.AutoMirrored.Filled.DirectionsRun
        WorkoutCategory.STRENGTH.displayName.lowercase() -> Icons.Default.FitnessCenter
        WorkoutCategory.FLEXIBILITY_MOBILITY.displayName.lowercase() -> Icons.Default.SelfImprovement
        WorkoutCategory.MIND_BODY_RECOVERY.displayName.lowercase() -> Icons.Default.SelfImprovement

        else -> Icons.Default.FitnessCenter
    }
}

