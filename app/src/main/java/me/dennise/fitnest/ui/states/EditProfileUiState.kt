package me.dennise.fitnest.ui.states

data class EditProfileUiState(
    val password: String = "",
    val passwordError: String? = null,
    val passwordVisible: Boolean = false,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val confirmPasswordVisible: Boolean = false,
    val mobile: String = "",
    val mobileError: String? = null,
    val receiveUpdates: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)
