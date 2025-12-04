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
import me.dennise.fitnest.data.UserRepository
import me.dennise.fitnest.ui.states.ViewProfileUiState

class ViewProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(ViewProfileUiState())
    val uiState: StateFlow<ViewProfileUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
    }

    public fun loadCurrentUser() {
        viewModelScope.launch {
            val user = Session.getCurrentUser()
            if (user != null) {
                _uiState.update {
                    it.copy(
                        id = user.id,
                        username = user.username,
                        email = user.email,
                        gender = user.gender,
                        mobile = user.mobile,
                        yearOfBirth = user.yearOfBirth,
                        receiveUpdates = user.receiveUpdates
                    )
                }
            }
        }
    }
}