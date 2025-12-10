package me.dennise.fitnest.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.dennise.fitnest.Session
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.types.WorkoutCategory
import me.dennise.fitnest.data.types.WorkoutEnjoyment
import me.dennise.fitnest.data.WorkoutRepository
import me.dennise.fitnest.data.entities.Workout
import me.dennise.fitnest.ui.states.HomeUiState
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
        loadWorkouts()
    }

    fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = Session.getCurrentUserId()
            if (userId != null) {
                val workouts = workoutRepository.getWorkouts(userId)
                _uiState.update { it.copy(workouts = workouts, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
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
                    category = WorkoutCategory.CARDIO.label,
                    duration = 45,
                    date = dateFormat.format(calendar.time),
                    time = "07:00 AM",
                    comments = "Great morning run in the park",
                    enjoyment = WorkoutEnjoyment.ENERGIZING.label
                )
            )

            // Sample Workout 2
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Evening Cycling",
                    category = WorkoutCategory.FLEXIBILITY_MOBILITY.label,
                    duration = 60,
                    date = dateFormat.format(calendar.time),
                    time = "06:00 PM",
                    comments = "Explored new trails",
                    enjoyment = WorkoutEnjoyment.CALMING.label
                )
            )

            // Sample Workout 3
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Yoga Session",
                    category = WorkoutCategory.MIND_BODY_RECOVERY.label,
                    duration = 30,
                    date = dateFormat.format(calendar.time),
                    time = "08:00 AM",
                    comments = "Relaxing yoga practice",
                    enjoyment = WorkoutEnjoyment.CALMING.label
                )
            )

            // Sample Workout 4
            calendar.add(Calendar.DAY_OF_YEAR, -2)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Swimming Laps",
                    category = WorkoutCategory.CARDIO.label,
                    duration = 40,
                    date = dateFormat.format(calendar.time),
                    time = "05:30 PM",
                    comments = "Improved my technique",
                    enjoyment = WorkoutEnjoyment.MOTIVATING.label
                )
            )

            // Sample Workout 5
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            workoutRepository.addWorkout(
                Workout(
                    userId = userId,
                    name = "Strength Training",
                    category = WorkoutCategory.STRENGTH.label,
                    duration = 75,
                    date = dateFormat.format(calendar.time),
                    time = "06:00 AM",
                    comments = "Full body workout",
                    enjoyment = WorkoutEnjoyment.EXHAUSTING.label
                )
            )

            loadWorkouts()
        }
    }
}