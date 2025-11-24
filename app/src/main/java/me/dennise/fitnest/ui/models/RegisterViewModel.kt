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
        state = state.copy(userName = value, userNameError = null)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value, passwordError = null)
    }

    fun togglePasswordVisibility() {
        state = state.copy(passwordVisible = !state.passwordVisible)
    }

    fun updateConfirmPassword(value: String) {
        state = state.copy(confirmPassword = value, confirmPasswordError = null)
    }

    fun toggleConfirmPasswordVisibility() {
        state = state.copy(confirmPasswordVisible = !state.confirmPasswordVisible)
    }

    fun updateEmail(value: String) {
        state = state.copy(email = value, emailError = null)
    }

    fun updateSelectedGender(value: String) {
        state = state.copy(selectedGender = value)
    }

    fun updateMobileNumber(value: String) {
        // Only allow digits
        if (value.all { it.isDigit() }) {
            state = state.copy(mobileNumber = value, mobileNumberError = null)
        }
    }

    fun updateReceiveUpdates(value: Boolean) {
        state = state.copy(receiveUpdates = value)
    }

    fun updateYearOfBirth(value: String) {
        state = state.copy(yearOfBirth = value, yearOfBirthError = null)
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Clear all previous errors
        state = state.copy(
            userNameError = null,
            passwordError = null,
            confirmPasswordError = null,
            emailError = null,
            genderError = null,
            mobileNumberError = null,
            yearOfBirthError = null
        )

        var hasError = false

        // Validate all required fields
        if (state.userName.isBlank()) {
            state = state.copy(userNameError = "User ID is required")
            hasError = true
        }

        if (state.password.isBlank()) {
            state = state.copy(passwordError = "Password is required")
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            state = state.copy(confirmPasswordError = "Please confirm password")
            hasError = true
        } else if (state.password != state.confirmPassword) {
            state = state.copy(confirmPasswordError = "Passwords do not match")
            hasError = true
        }

        if (state.email.isBlank()) {
            state = state.copy(emailError = "Email is required")
            hasError = true
        }

        if (state.selectedGender.isBlank()) {
            state = state.copy(genderError = "Please select a gender")
            hasError = true
        }

        if (state.mobileNumber.isBlank()) {
            state = state.copy(mobileNumberError = "Mobile number is required")
            hasError = true
        }

        if (state.yearOfBirth.isBlank()) {
            state = state.copy(yearOfBirthError = "Year of birth is required")
            hasError = true
        }

        if (hasError) {
            return
        }

        state = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = userRepository.getUser(state.userName)
                if (existingUser != null) {
                    state = state.copy(
                        isLoading = false,
                        userNameError = "Username already exists"
                    )
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