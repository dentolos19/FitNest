package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.*
import me.dennise.fitnest.ui.models.EditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: EditProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Edit Profile",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar()

            Spacer(modifier = Modifier.height(16.dp))

            TextInput(
                value = uiState.mobile,
                onValueChange = viewModel::updateMobile,
                label = "Mobile Number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorText = uiState.mobileError
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordInput(
                password = uiState.password,
                onPasswordChange = viewModel::updatePassword,
                label = "New Password (optional)",
                errorText = uiState.passwordError,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = viewModel::updateConfirmPassword,
                label = "Confirm New Password",
                errorText = uiState.confirmPasswordError,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.toggleConfirmPasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BooleanInput(
                label = "Receive Updates",
                checked = uiState.receiveUpdates,
                onCheckedChange = viewModel::updateReceiveUpdates
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.save(
                        onSuccess = { /* Handled by LaunchedEffect */ },
                        onError = { /* Errors are displayed on screen */ }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Changes")
                }
            }
        }
    }
}
