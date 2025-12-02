package me.dennise.fitnest.ui.states

data class RegisterUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordVisible: Boolean = false,
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val confirmPasswordError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val selectedGender: String = "",
    val genderError: String? = null,
    val mobileNumber: String = "",
    val mobileNumberError: String? = null,
    val receiveUpdates: Boolean = false,
    val yearOfBirth: String = "",
    val yearOfBirthError: String? = null,
)