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
import me.dennise.fitnest.data.entities.User
import me.dennise.fitnest.ui.states.RegisterState

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    var state by mutableStateOf(RegisterState())
        private set

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
    }

    fun updateUserName(value: String) {
        state = state.copy(userName = value)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value)
    }

    fun togglePasswordVisibility() {
        state = state.copy(passwordVisible = !state.passwordVisible)
    }

    fun updateConfirmPassword(value: String) {
        state = state.copy(confirmPassword = value)
    }

    fun toggleConfirmPasswordVisibility() {
        state = state.copy(confirmPasswordVisible = !state.confirmPasswordVisible)
    }

    fun updateEmail(value: String) {
        state = state.copy(email = value)
    }

    fun updateSelectedGender(value: String) {
        state = state.copy(selectedGender = value)
    }

    fun updateMobileNumber(value: String) {
        // Only allow digits
        if (value.all { it.isDigit() }) {
            state = state.copy(mobileNumber = value)
        }
    }

    fun updateReceiveUpdates(value: Boolean) {
        state = state.copy(receiveUpdates = value)
    }

    fun updateYearOfBirth(value: String) {
        state = state.copy(yearOfBirth = value)
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Validate all required fields
        when {
            state.userName.isBlank() -> {
                onError("Please enter user ID")
                return
            }

            state.password.isBlank() -> {
                onError("Please enter password")
                return
            }

            state.confirmPassword.isBlank() -> {
                onError("Please confirm password")
                return
            }

            state.password != state.confirmPassword -> {
                onError("Passwords do not match")
                return
            }

            state.email.isBlank() -> {
                onError("Please enter email")
                return
            }

            state.selectedGender.isBlank() -> {
                onError("Please select gender")
                return
            }

            state.mobileNumber.isBlank() -> {
                onError("Please enter mobile number")
                return
            }

            state.yearOfBirth.isBlank() -> {
                onError("Please select year of birth")
                return
            }
        }

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = userRepository.getUser(state.userName)
                if (existingUser != null) {
                    state = state.copy(isLoading = false)
                    onError("Username already exists")
                    return@launch
                }

                // Create and insert new user
                val user = User(
                    username = state.userName,
                    password = state.password,
                    email = state.email,
                    gender = state.selectedGender,
                    mobile = state.mobileNumber,
                    yearOfBirth = state.yearOfBirth.toInt(),
                    receiveUpdates = state.receiveUpdates
                )

                userRepository.registerUser(user)

                // Get the newly created user with its ID and login
                val newUser = userRepository.getUser(state.userName)
                if (newUser != null) {
                    Session.loginUser(newUser)
                }

                state = state.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                onError("Registration failed: ${e.message}")
            }
        }
    }
}