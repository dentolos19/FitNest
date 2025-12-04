package me.dennise.fitnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.dennise.fitnest.data.AppDatabase
import me.dennise.fitnest.data.UserGender
import me.dennise.fitnest.data.entities.User
import me.dennise.fitnest.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize session management and demo data
        val sessionManager = SessionManager(this)
        Session.initialize(sessionManager)
        initializeDemoData()

        // Start user interface
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }

    private fun initializeDemoData() {
        scope.launch {
            val userDao = database.userDao()
            val demoUser = userDao.get("TestUser1")

            if (demoUser == null) {
                val user = User(
                    username = "TestUser1",
                    password = "TestPassword1",
                    email = "user@example.com",
                    gender = UserGender.MALE.label,
                    mobile = "81234567",
                    yearOfBirth = 2007,
                    receiveUpdates = false
                )
                userDao.insert(user)
            }
        }
    }
}