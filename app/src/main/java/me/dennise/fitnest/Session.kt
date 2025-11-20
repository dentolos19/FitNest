package me.dennise.fitnest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.dennise.fitnest.data.entities.User

object Session {
    private var currentUser: User? = null
    private var sessionManager: SessionManager? = null

    fun initialize(manager: SessionManager) {
        sessionManager = manager
    }

    suspend fun loginUser(user: User) {
        currentUser = user
        sessionManager?.saveSession(user)
    }

    suspend fun logoutUser() {
        currentUser = null
        withContext(Dispatchers.IO) {
            sessionManager?.clearSession()
        }
    }

    suspend fun restoreSession(): User? {
        if (currentUser != null) {
            return currentUser
        }

        val user = sessionManager?.restoreSession()
        currentUser = user
        return user
    }

    fun getCurrentUser(): User? = currentUser

    fun getCurrentUserId(): Int? = currentUser?.id

    fun isLoggedIn(): Boolean = currentUser != null
}