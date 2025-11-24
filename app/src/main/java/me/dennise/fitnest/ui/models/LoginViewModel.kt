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
        state = state.copy(username = value)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value)
    }

    fun togglePasswordVisibility() {
        state = state.copy(passwordVisible = !state.passwordVisible)
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Validate user ID and password inputs
        if (state.username.isBlank() || state.password.isBlank()) {
            onError("Please enter both User ID and Password")
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
                    state = state.copy(isLoading = false)
                    onError("Invalid username or password")
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                onError("Login failed: ${e.message}")
            }
        }
    }
}