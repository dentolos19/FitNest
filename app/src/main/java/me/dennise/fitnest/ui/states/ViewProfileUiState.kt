package me.dennise.fitnest.ui.states

data class ViewProfileUiState(
    val username: String = "",
    val email: String = "",
    val gender: String = "",
    val mobile: String = "",
    val yearOfBirth: Int = 0,
    val receiveUpdates: Boolean = false,
    val loading: Boolean = false
)