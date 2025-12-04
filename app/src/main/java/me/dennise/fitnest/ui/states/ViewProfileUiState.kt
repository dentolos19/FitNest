package me.dennise.fitnest.ui.states

data class ViewProfileUiState(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val gender: String = "",
    val mobile: String = "",
    val yearOfBirth: Int = 0,
    val receiveUpdates: Boolean = false
)