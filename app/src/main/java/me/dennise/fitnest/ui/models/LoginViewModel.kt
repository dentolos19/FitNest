package me.dennise.fitnest.ui.models

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dennise.fitnest.Session
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.UserRepository
import me.dennise.fitnest.ui.states.LoginState

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    var state by mutableStateOf(LoginState())
        private set

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
    }

    fun updateUsername(value: String) {
        state = state.copy(username = value, usernameError = null)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value, passwordError = null)
    }

    fun togglePasswordVisibility() {
        state = state.copy(passwordVisible = !state.passwordVisible)
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Clear previous errors
        state = state.copy(usernameError = null, passwordError = null)

        var hasError = false

        // Validate user ID
        if (state.username.isBlank()) {
            state = state.copy(usernameError = "User ID is required")
            hasError = true
        }

        // Validate password
        if (state.password.isBlank()) {
            state = state.copy(passwordError = "Password is required")
            hasError = true
        }

        if (hasError) {
            return
        }

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Check database credentials
                val user = userRepository.loginUser(state.username, state.password)
                if (user != null) {
                    // Login success
                    Session.loginUser(user)
                    state = state.copy(isLoading = false)
                    onSuccess()
                } else {
                    // Login failure
                    state = state.copy(
                        isLoading = false,
                        passwordError = "Invalid username or password"
                    )
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                onError("Login failed: ${e.message}")
            }
        }
    }
}