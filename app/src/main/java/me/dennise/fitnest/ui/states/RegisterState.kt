package me.dennise.fitnest.ui.states

data class RegisterState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val email: String = "",
    val selectedGender: String = "",
    val mobileNumber: String = "",
    val receiveUpdates: Boolean = false,
    val yearOfBirth: String = "",
)