package me.dennise.fitnest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.Workout
import me.dennise.fitnest.data.WorkoutRepository
import java.text.SimpleDateFormat
import java.util.*

data class AddWorkoutUiState(
    val name: String = "",
    val category: String = WorkoutCategory.CARDIO.displayName,
    val duration: String = "",
    val comments: String = "",
    val enjoyment: String = EnjoymentRating.ENERGIZING.displayName,
    val enjoymentIndex: Int = 0,
    val date: String = "",
    val time: String = "",
    val nameError: String? = null
)

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
            enjoyment = enjoymentRating.displayName,
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

    fun validateAndSave(): Boolean {
        val state = _uiState.value

        // Validate name (required)
        if (state.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Name is required")
            return false
        }

        // Category is always selected (has default), so no validation needed

        // Save workout
        viewModelScope.launch {
            val workout = Workout(
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

