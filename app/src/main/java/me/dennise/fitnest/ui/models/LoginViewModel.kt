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
import me.dennise.fitnest.ui.states.LoginUiState

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
    }

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Clear all previous errors
        _uiState.update { it.copy(usernameError = null, passwordError = null) }

        val currentState = _uiState.value
        var hasError = false

        // Validate username
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Username is required") }
            hasError = true
        }

        // Validate password
        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }

        if (hasError) {
            onError("Please enter all required fields!")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Check database credentials
                val user = userRepository.loginUser(currentState.username, currentState.password)
                if (user != null) {
                    // Login success
                    Session.login(user)
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                } else {
                    // Login failure
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordError = "Invalid username or password"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError("Login failed: ${e.message}")
            }
        }
    }
}