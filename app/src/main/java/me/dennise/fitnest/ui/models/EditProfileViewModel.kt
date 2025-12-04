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
import me.dennise.fitnest.ui.states.EditProfileUiState

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val user = Session.getCurrentUser()
            if (user != null) {
                _uiState.update {
                    it.copy(
                        mobile = user.mobile,
                        receiveUpdates = user.receiveUpdates
                    )
                }
            }
        }
    }

    fun updateMobile(value: String) {
        _uiState.update { it.copy(mobile = value, mobileError = null) }
    }

    fun updateReceiveUpdates(value: Boolean) {
        _uiState.update { it.copy(receiveUpdates = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun updateConfirmPassword(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun save() {
        val currentState = _uiState.value
        var hasError = false

        // Validate mobile
        if (currentState.mobile.isBlank()) {
            _uiState.update { it.copy(mobileError = "Mobile number is required") }
            hasError = true
        }

        // Validate password
        if (currentState.password.isNotBlank() || currentState.confirmPassword.isNotBlank()) {
            if (currentState.password.length < 8) {
                _uiState.update { it.copy(passwordError = "Password must be at least 8 characters") }
                hasError = true
            }
            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
                hasError = true
            }
        }

        if (hasError) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val currentUser = Session.getCurrentUser() ?: return@launch
            val updatedUser = currentUser.copy(
                mobile = currentState.mobile,
                receiveUpdates = currentState.receiveUpdates,
                password = if (currentState.password.isNotBlank()) currentState.password else currentUser.password
            )
            userRepository.updateUser(updatedUser)
            Session.login(updatedUser)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}