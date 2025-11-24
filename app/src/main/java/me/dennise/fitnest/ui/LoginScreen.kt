package me.dennise.fitnest.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.R
import me.dennise.fitnest.ui.components.PasswordInput
import me.dennise.fitnest.ui.components.TextInput
import me.dennise.fitnest.ui.models.LoginViewModel
import me.dennise.fitnest.ui.theme.AppTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            // Title
            Text(
                text = "FitNest",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle
            Text(
                text = "Login to your account",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // User ID Field
            TextInput(
                value = viewModel.state.username,
                onValueChange = viewModel::updateUsername,
                label = "User ID",
                placeholder = "Enter your User ID",
                errorText = viewModel.state.usernameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = !viewModel.state.isLoading
            )

            // Password Field
            PasswordInput(
                password = viewModel.state.password,
                onPasswordChange = viewModel::updatePassword,
                passwordVisible = viewModel.state.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() },
                errorText = viewModel.state.passwordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                enabled = !viewModel.state.isLoading
            )

            // Login Button
            Button(
                onClick = {
                    viewModel.login(
                        onSuccess = {
                            Toast.makeText(
                                context,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onLoginSuccess()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(
                                context,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !viewModel.state.isLoading
            ) {
                if (viewModel.state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !viewModel.state.isLoading
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Test Credentials Info
            Text(
                text = "Test credentials: TestUser1 / TestPassword1",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}