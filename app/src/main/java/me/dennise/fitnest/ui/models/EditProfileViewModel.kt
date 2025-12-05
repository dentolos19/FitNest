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
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = Session.getCurrentUser()
            if (user != null) {
                _uiState.update {
                    it.copy(
                        mobileNumber = user.mobile,
                        receiveUpdates = user.receiveUpdates
                    )
                }
            }
        }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun updateConfirmPassword(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun updateMobileNumber(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(mobileNumber = value, mobileNumberError = null) }
        }
    }

    fun updateReceiveUpdates(value: Boolean) {
        _uiState.update { it.copy(receiveUpdates = value) }
    }

    fun updateProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                passwordError = null,
                confirmPasswordError = null,
                mobileNumberError = null
            )
        }

        val currentState = _uiState.value
        var hasError = false

        if (currentState.password.isNotBlank()) {
            if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
                onError("Passwords do not match")
                hasError = true
            }
        }

        if (currentState.mobileNumber.isBlank()) {
            _uiState.update { it.copy(mobileNumberError = "Mobile number is required") }
            hasError = true
        }

        if (hasError) {
            onError("Please enter all required fields!")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val currentUser = Session.getCurrentUser()
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        password = if (currentState.password.isNotBlank()) currentState.password else currentUser.password,
                        mobile = currentState.mobileNumber,
                        receiveUpdates = currentState.receiveUpdates
                    )
                    userRepository.updateUser(updatedUser)
                    Session.login(updatedUser)
                    _uiState.update { it.copy(isLoading = false, isProfileUpdated = true) }
                    onSuccess()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError("Update failed: ${e.message}")
            }
        }
    }
}
