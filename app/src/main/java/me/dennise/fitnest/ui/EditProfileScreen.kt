package me.dennise.fitnest.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.*
import me.dennise.fitnest.ui.models.EditProfileViewModel

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Edit Profile",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.updateProfile(
                                onSuccess = {},
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Save"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Avatar()

            PasswordInput(
                password = uiState.password,
                onPasswordChange = viewModel::onPasswordChange,
                label = "New Password",
                placeholder = "Enter new password",
                errorText = uiState.passwordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = { viewModel.setPasswordVisibility(it) }
            )

            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = viewModel::onConfirmPasswordChange,
                label = "Confirm New Password",
                placeholder = "Re-enter new password",
                errorText = uiState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.setConfirmPasswordVisibility(it) }
            )

            TextInput(
                value = uiState.mobile,
                onValueChange = viewModel::onMobileChange,
                label = "Mobile Number",
                placeholder = "Enter mobile number",
                errorText = uiState.mobileError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            BooleanInput(
                label = "To receive updates via email",
                checked = uiState.receiveUpdates,
                onCheckedChange = viewModel::onReceiveUpdatesChange,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}