package me.dennise.fitnest

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.dennise.fitnest.ui.HomeScreen
import me.dennise.fitnest.ui.LoginScreen
import me.dennise.fitnest.ui.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to home/landing screen after successful login
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // Navigate to register screen
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navigate to home/landing screen after successful registration
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onCancel = {
                    // Navigate back to login screen when cancel is clicked
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen()
        }
    }
}