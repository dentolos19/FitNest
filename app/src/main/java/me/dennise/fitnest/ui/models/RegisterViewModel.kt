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
import me.dennise.fitnest.data.entities.User
import me.dennise.fitnest.ui.states.RegisterUiState

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

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

    fun updateConfirmPassword(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun updateEmail(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun updateSelectedGender(value: String) {
        _uiState.update { it.copy(selectedGender = value) }
    }

    fun updateMobileNumber(value: String) {
        // Only allow digits
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(mobileNumber = value, mobileNumberError = null) }
        }
    }

    fun updateReceiveUpdates(value: Boolean) {
        _uiState.update { it.copy(receiveUpdates = value) }
    }

    fun updateYearOfBirth(value: String) {
        _uiState.update { it.copy(yearOfBirth = value, yearOfBirthError = null) }
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Clear all previous errors
        _uiState.update {
            it.copy(
                usernameError = null,
                passwordError = null,
                confirmPasswordError = null,
                emailError = null,
                genderError = null,
                mobileNumberError = null,
                yearOfBirthError = null
            )
        }

        val currentState = _uiState.value
        var hasError = false

        // Validate all required fields
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "User ID is required") }
            hasError = true
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }

        if (currentState.confirmPassword.isBlank()) {
            _uiState.update { it.copy(confirmPasswordError = "Please confirm password") }
            hasError = true
        } else if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            onError("Passwords do not match")
            hasError = true
        }

        if (currentState.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email is required") }
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update { it.copy(emailError = "Invalid email format") }
            hasError = true
        }

        if (currentState.selectedGender.isBlank()) {
            _uiState.update { it.copy(genderError = "Please select a gender") }
            hasError = true
        }

        if (currentState.mobileNumber.isBlank()) {
            _uiState.update { it.copy(mobileNumberError = "Mobile number is required") }
            hasError = true
        }

        if (currentState.yearOfBirth.isBlank()) {
            _uiState.update { it.copy(yearOfBirthError = "Year of birth is required") }
            hasError = true
        }

        if (hasError) {
            onError("Please enter all required fields!")
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = userRepository.getUser(currentState.username)
                if (existingUser != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            usernameError = "Username already exists"
                        )
                    }
                    onError("Username already exists")
                    return@launch
                }

                // Create and insert new user
                val user = User(
                    username = currentState.username,
                    password = currentState.password,
                    email = currentState.email,
                    gender = currentState.selectedGender,
                    mobile = currentState.mobileNumber,
                    yearOfBirth = currentState.yearOfBirth.toInt(),
                    receiveUpdates = currentState.receiveUpdates
                )

                userRepository.registerUser(user)

                // Get the newly created user with its ID and login
                val newUser = userRepository.getUser(currentState.username)
                if (newUser != null) {
                    Session.login(newUser)
                }

                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                onError("Registration failed: ${e.message}")
            }
        }
    }
}