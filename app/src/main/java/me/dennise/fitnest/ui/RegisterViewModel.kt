package me.dennise.fitnest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.User
import me.dennise.fitnest.data.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDao = AppDatabase.Companion.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun registerUser(
        username: String,
        password: String,
        email: String,
        gender: String,
        mobile: String,
        yearOfBirth: Int,
        receiveUpdates: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = repository.getUser(username)
                if (existingUser != null) {
                    onError("Username already exists")
                    return@launch
                }

                // Create and insert new user
                val user = User(
                    username = username,
                    password = password,
                    email = email,
                    gender = gender,
                    mobile = mobile,
                    yearOfBirth = yearOfBirth,
                    receiveUpdates = receiveUpdates
                )

                repository.registerUser(user)
                onSuccess()
            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }
}