package me.dennise.fitnest.ui.states

data class EditProfileUiState(
    val mobile: String = "",
    val receiveUpdates: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val mobileError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)