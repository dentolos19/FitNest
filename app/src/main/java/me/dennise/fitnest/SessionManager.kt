package me.dennise.fitnest

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.User

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val dao = AppDatabase.Companion.getDatabase(context).userDao()

    suspend fun saveSession(user: User) {
        withContext(Dispatchers.IO) {
            prefs.edit().apply {
                putString(KEY_USERNAME, user.username)
                apply()
            }
        }
    }

    suspend fun restoreSession(): User? {
        return withContext(Dispatchers.IO) {
            if (!prefs.contains(KEY_USERNAME)) {
                return@withContext null
            }

            val username = prefs.getString(KEY_USERNAME, null) ?: return@withContext null
            val user = dao.get(username)

            if (user != null) {
                user
            } else {
                clearSession()
                null
            }
        }
    }

    fun clearSession() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "session"
        private const val KEY_USERNAME = "username"
    }
}