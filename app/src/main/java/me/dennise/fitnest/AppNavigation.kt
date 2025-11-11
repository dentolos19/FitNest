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
            LoginScreen()
        }

        composable("register") {
            RegisterScreen()
        }

        composable("home") {
            HomeScreen()
        }
    }
}