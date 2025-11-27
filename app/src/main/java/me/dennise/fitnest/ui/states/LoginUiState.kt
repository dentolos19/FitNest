package me.dennise.fitnest.ui.states

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordVisible: Boolean = false,
    val passwordError: String? = null,
)