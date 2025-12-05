package me.dennise.fitnest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import me.dennise.fitnest.ui.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    var isCheckingSession by remember { mutableStateOf(true) }
    var startDestination by remember { mutableStateOf(Routes.LOGIN) }

    LaunchedEffect(Unit) {
        scope.launch {
            val user = Session.restoreSession()
            startDestination = if (user != null) {
                Routes.HOME
            } else {
                Routes.LOGIN
            }
            isCheckingSession = false
        }
    }

    if (isCheckingSession) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onCancel = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onViewProfile = {
                    navController.navigate(Routes.PROFILE_DETAIL)
                },
                onAddWorkout = {
                    navController.navigate(Routes.WORKOUT_ADD)
                },
                onWorkoutClick = { workoutId ->
                    navController.navigate(Routes.workoutDetail(workoutId))
                }
            )
        }

        composable(Routes.PROFILE_DETAIL) {
            ViewProfileScreen(
                onNavigateBack = { navController.navigateUp() },
                onEditProfile = {
                    navController.navigate(Routes.PROFILE_EDIT)
                }
            )
        }

        composable(Routes.PROFILE_EDIT) {
            EditProfileScreen(
                onUpdateSuccess = {
                    navController.navigate(Routes.PROFILE_DETAIL) {
                        popUpTo(Routes.PROFILE_DETAIL) { inclusive = true }
                    }
                },
                onCancel = { navController.navigateUp() }
            )
        }

        composable(Routes.WORKOUT_ADD) {
            AddWorkoutScreen(
                onNavigateBack = { navController.navigateUp() },
                onWorkoutAdded = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Routes.WORKOUT_DETAIL,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
            ViewWorkoutScreen(
                workoutId = workoutId,
                onNavigateBack = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}