package me.dennise.fitnest.ui.states

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val mobile: String = "",
    val mobileError: String? = null,
    val password: String = "",
    val passwordVisible: Boolean = false,
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val confirmPasswordError: String? = null,
    val receiveUpdates: Boolean = false,
)