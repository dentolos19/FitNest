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
        loadInitialData()
    }

    private fun loadInitialData() {
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

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun onMobileChange(mobile: String) {
        if (mobile.all { it.isDigit() }) {
            _uiState.update { it.copy(mobile = mobile, mobileError = null) }
        }
    }

    fun onReceiveUpdatesChange(receiveUpdates: Boolean) {
        _uiState.update { it.copy(receiveUpdates = receiveUpdates) }
    }

    fun setPasswordVisibility(visible: Boolean) {
        _uiState.update { it.copy(passwordVisible = visible) }
    }

    fun setConfirmPasswordVisibility(visible: Boolean) {
        _uiState.update { it.copy(confirmPasswordVisible = visible) }
    }

    fun updateProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _uiState.update {
            it.copy(
                passwordError = null,
                confirmPasswordError = null,
                mobileError = null,
                isLoading = true,
                isSuccess = false
            )
        }

        var hasError = false

        if (_uiState.value.password.isNotBlank() && _uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }

        if (_uiState.value.mobile.isBlank()) {
            _uiState.update { it.copy(mobileError = "Mobile number is required") }
            hasError = true
        }

        if (hasError) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
            try {
                val currentUser = Session.getCurrentUser()
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        password = if (_uiState.value.password.isNotBlank()) _uiState.value.password else currentUser.password,
                        mobile = _uiState.value.mobile,
                        receiveUpdates = _uiState.value.receiveUpdates
                    )
                    userRepository.updateUser(updatedUser)
                    Session.loginUser(updatedUser)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    onSuccess()
                } else {
                    onError("User not logged in")
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                onError("Failed to update profile: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
