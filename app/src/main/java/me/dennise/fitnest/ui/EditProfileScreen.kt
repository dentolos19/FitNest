package me.dennise.fitnest.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
    onUpdateSuccess: () -> Unit = {},
    onCancel: () -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isProfileUpdated) {
        if (uiState.isProfileUpdated) {
            onUpdateSuccess()
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Edit Profile",
                canNavigateBack = true,
                onNavigateBack = onCancel,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.updateProfile(
                                onSuccess = {
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
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

            // Password Field
            PasswordInput(
                password = uiState.password,
                onPasswordChange = viewModel::updatePassword,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() },
                label = "New Password",
                placeholder = "Enter new password",
                errorText = uiState.passwordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Confirm Password Field
            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = viewModel::updateConfirmPassword,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.toggleConfirmPasswordVisibility() },
                label = "Confirm New Password",
                placeholder = "Re-enter new password",
                errorText = uiState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Mobile Number Field
            TextInput(
                value = uiState.mobileNumber,
                onValueChange = viewModel::updateMobileNumber,
                label = "Mobile Number",
                placeholder = "Enter mobile number",
                errorText = uiState.mobileNumberError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Receive Updates Checkbox
            BooleanInput(
                label = "To receive updates via email",
                checked = uiState.receiveUpdates,
                onCheckedChange = viewModel::updateReceiveUpdates,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
