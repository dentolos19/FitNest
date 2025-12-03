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
import me.dennise.fitnest.ui.states.AddWorkoutUiState
import java.text.SimpleDateFormat
import java.util.*

class AddWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _uiState = MutableStateFlow(AddWorkoutUiState())
    val uiState: StateFlow<AddWorkoutUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateDuration(duration: String) {
        // Only allow numeric input
        if (duration.isEmpty() || duration.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(duration = duration)
        }
    }

    fun updateComments(comments: String) {
        _uiState.value = _uiState.value.copy(comments = comments)
    }

    fun updateEnjoyment(index: Int) {
        val enjoymentRating = EnjoymentRating.entries[index]
        _uiState.value = _uiState.value.copy(
            enjoyment = enjoymentRating.label,
            enjoymentIndex = index
        )
    }

    fun updateDate(dateMillis: Long) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = Date(dateMillis)
        _uiState.value = _uiState.value.copy(date = dateFormat.format(date))
    }

    fun updateTime(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        _uiState.value = _uiState.value.copy(time = timeFormat.format(calendar.time))
    }

    fun save(): Boolean {
        val currentState = _uiState.value

        // Validate name (required)
        if (currentState.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Name is required")
            return false
        }

        // Get current user ID
        val userId = Session.getCurrentUserId() ?: return false

        // Save workout
        viewModelScope.launch {
            val workout = Workout(
                userId = userId,
                name = currentState.name,
                category = currentState.category,
                duration = currentState.duration.toIntOrNull(),
                date = currentState.date.ifBlank { null },
                time = currentState.time.ifBlank { null },
                comments = currentState.comments.ifBlank { null },
                enjoyment = currentState.enjoyment
            )
            workoutRepository.addWorkout(workout)
        }

        return true
    }
}