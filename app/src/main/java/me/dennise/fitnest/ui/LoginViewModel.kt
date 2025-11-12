package me.dennise.fitnest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.FitNestDatabase
import me.dennise.fitnest.data.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDao = FitNestDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun loginUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Check hardcoded credentials first
                if (username == "TestUser1" && password == "TestPassword1") {
                    onSuccess()
                    return@launch
                }

                // Check database credentials
                val user = repository.loginUser(username, password)
                if (user != null) {
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

