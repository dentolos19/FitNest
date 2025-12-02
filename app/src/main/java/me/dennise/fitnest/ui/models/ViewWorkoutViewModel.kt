package me.dennise.fitnest.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.WorkoutRepository
import me.dennise.fitnest.ui.states.ViewWorkoutUiState

class ViewWorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _state = MutableStateFlow(ViewWorkoutUiState())
    val state: StateFlow<ViewWorkoutUiState> = _state.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
    }

    fun loadWorkout(workoutId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val workout = workoutRepository.getWorkout(workoutId)
            _state.value = _state.value.copy(
                workout = workout,
                isLoading = false
            )
        }
    }

    fun deleteWorkout() {
        viewModelScope.launch {
            _state.value.workout?.let { workout ->
                workoutRepository.deleteWorkout(workout)
                _state.value = _state.value.copy(isDeleted = true)
            }
        }
    }
}