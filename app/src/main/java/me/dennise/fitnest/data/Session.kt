package me.dennise.fitnest.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun logout() {
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

