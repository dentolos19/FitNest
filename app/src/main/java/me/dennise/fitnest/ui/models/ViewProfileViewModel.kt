package me.dennise.fitnest.ui.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.dennise.fitnest.Session
import me.dennise.fitnest.ui.states.ViewProfileUiState

class ViewProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ViewProfileUiState())
    val uiState: StateFlow<ViewProfileUiState> = _uiState.asStateFlow()

    fun loadUser() {
        val user = Session.getCurrentUser()
        if (user != null) {
            _uiState.update {
                it.copy(
                    username = user.username,
                    email = user.email,
                    gender = user.gender,
                    mobile = user.mobile,
                    yearOfBirth = user.yearOfBirth,
                    receiveUpdates = user.receiveUpdates
                )
            }
        }
    }
}
