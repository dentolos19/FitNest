package me.dennise.fitnest

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.dennise.fitnest.ui.AddWorkoutScreen
import me.dennise.fitnest.ui.HomeScreen
import me.dennise.fitnest.ui.LoginScreen
import me.dennise.fitnest.ui.RegisterScreen
import me.dennise.fitnest.ui.WorkoutDetailScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val ADD_WORKOUT = "add_workout"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"

    fun workoutDetail(workoutId: Int) = "workout_detail/$workoutId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to home/landing screen after successful login
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // Navigate to register screen
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navigate to home/landing screen after successful registration
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                    }
                },
                onCancel = {
                    // Navigate back to login screen when cancel is clicked
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onViewProfile = {
                    navController.navigate(AppRoutes.PROFILE)
                },
                onAddWorkout = {
                    navController.navigate(AppRoutes.ADD_WORKOUT)
                },
                onWorkoutClick = { workoutId ->
                    navController.navigate(AppRoutes.workoutDetail(workoutId))
                }
            )
        }

        composable(AppRoutes.PROFILE) {
            // Placeholder for Profile Screen
            PlaceholderScreen(
                title = "Profile Screen",
                onBack = { navController.navigateUp() }
            )
        }

        composable(AppRoutes.ADD_WORKOUT) {
            AddWorkoutScreen(
                onNavigateBack = { navController.navigateUp() },
                onWorkoutAdded = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = AppRoutes.WORKOUT_DETAIL,
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
            WorkoutDetailScreen(
                workoutId = workoutId,
                onNavigateBack = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    title: String,
    subtitle: String = "",
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
                if (subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}