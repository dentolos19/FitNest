package me.dennise.fitnest.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dennise.fitnest.Session
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
    }

    fun login(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Check database credentials
                val user = userRepository.loginUser(username, password)
                if (user != null) {
                    Session.loginUser(user)
                    onSuccess()
                } else {
                    onError("Invalid username or password")
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            }
        }
    }
}