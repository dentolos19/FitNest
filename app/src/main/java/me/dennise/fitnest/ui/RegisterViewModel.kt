package me.dennise.fitnest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.Session
import me.dennise.fitnest.data.User
import me.dennise.fitnest.data.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
    }

    fun register(
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

                // Get the newly created user with its ID and login
                val newUser = repository.getUser(username)
                if (newUser != null) {
                    Session.loginUser(newUser)
                }

                onSuccess()
            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }
}