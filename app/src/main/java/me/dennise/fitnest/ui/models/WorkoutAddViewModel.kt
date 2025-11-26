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
import me.dennise.fitnest.ui.states.WorkoutAddState
import java.text.SimpleDateFormat
import java.util.*

class WorkoutAddViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _state = MutableStateFlow(WorkoutAddState())
    val state: StateFlow<WorkoutAddState> = _state.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
    }

    fun updateName(name: String) {
        _state.value = _state.value.copy(
            name = name,
            nameError = null
        )
    }

    fun updateCategory(category: String) {
        _state.value = _state.value.copy(category = category)
    }

    fun updateDuration(duration: String) {
        // Only allow numeric input
        if (duration.isEmpty() || duration.all { it.isDigit() }) {
            _state.value = _state.value.copy(duration = duration)
        }
    }

    fun updateComments(comments: String) {
        _state.value = _state.value.copy(comments = comments)
    }

    fun updateEnjoyment(index: Int) {
        val enjoymentRating = EnjoymentRating.entries[index]
        _state.value = _state.value.copy(
            enjoyment = enjoymentRating.displayName,
            enjoymentIndex = index
        )
    }

    fun updateDate(dateMillis: Long) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = Date(dateMillis)
        _state.value = _state.value.copy(date = dateFormat.format(date))
    }

    fun updateTime(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        _state.value = _state.value.copy(time = timeFormat.format(calendar.time))
    }

    fun validateAndSave(): Boolean {
        val state = _state.value

        // Validate name (required)
        if (state.name.isBlank()) {
            _state.value = _state.value.copy(nameError = "Name is required")
            return false
        }

        // Get current user ID
        val userId = Session.getCurrentUserId() ?: return false

        // Save workout
        viewModelScope.launch {
            val workout = Workout(
                userId = userId,
                name = state.name,
                category = state.category,
                duration = state.duration.toIntOrNull(),
                date = state.date.ifBlank { null },
                time = state.time.ifBlank { null },
                comments = state.comments.ifBlank { null },
                enjoyment = state.enjoyment
            )
            workoutRepository.addWorkout(workout)
        }

        return true
    }
}