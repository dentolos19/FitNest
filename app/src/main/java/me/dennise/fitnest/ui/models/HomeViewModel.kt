package me.dennise.fitnest.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.dennise.fitnest.Session
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.EnjoymentRating
import me.dennise.fitnest.data.WorkoutRepository
import me.dennise.fitnest.data.entities.Workout
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            val userId = Session.getCurrentUserId()
            if (userId != null) {
                _workouts.value = workoutRepository.getWorkouts(userId)
            }
        }
    }

    fun addSampleWorkouts() {
        viewModelScope.launch {
            val userId = Session.getCurrentUserId() ?: return@launch
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()

            // Sample Workout 1
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Morning Run",
                    category = "Running",
                    duration = 45,
                    date = dateFormat.format(calendar.time),
                    time = "07:00 AM",
                    comments = "Great morning run in the park",
                    enjoyment = EnjoymentRating.ENERGIZING.displayName
                )
            )

            // Sample Workout 2
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Evening Cycling",
                    category = "Cycling",
                    duration = 60,
                    date = dateFormat.format(calendar.time),
                    time = "06:00 PM",
                    comments = "Explored new trails",
                    enjoyment = EnjoymentRating.CALMING.displayName
                )
            )

            // Sample Workout 3
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Yoga Session",
                    category = "Yoga",
                    duration = 30,
                    date = dateFormat.format(calendar.time),
                    time = "08:00 AM",
                    comments = "Relaxing yoga practice",
                    enjoyment = EnjoymentRating.CALMING.displayName
                )
            )

            // Sample Workout 4
            calendar.add(Calendar.DAY_OF_YEAR, -2)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Swimming Laps",
                    category = "Swimming",
                    duration = 40,
                    date = dateFormat.format(calendar.time),
                    time = "05:30 PM",
                    comments = "Improved my technique",
                    enjoyment = EnjoymentRating.MOTIVATING.displayName
                )
            )

            // Sample Workout 5
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Strength Training",
                    category = "Gym",
                    duration = 75,
                    date = dateFormat.format(calendar.time),
                    time = "06:00 AM",
                    comments = "Full body workout",
                    enjoyment = EnjoymentRating.EXHAUSTING.displayName
                )
            )

            loadWorkouts()
        }
    }
}