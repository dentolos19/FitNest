package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Avatar()

            TextInput(
                value = uiState.mobile,
                onValueChange = viewModel::updateMobile,
                label = "Mobile Number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorText = uiState.mobileError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            PasswordInput(
                password = uiState.password,
                onPasswordChange = viewModel::updatePassword,
                label = "New Password (optional)",
                errorText = uiState.passwordError,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = viewModel::updateConfirmPassword,
                label = "Confirm New Password",
                errorText = uiState.confirmPasswordError,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.toggleConfirmPasswordVisibility() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            BooleanInput(
                label = "Receive Updates",
                checked = uiState.receiveUpdates,
                onCheckedChange = viewModel::updateReceiveUpdates,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::save,
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