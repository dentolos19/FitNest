package me.dennise.fitnest.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.*
import me.dennise.fitnest.ui.models.RegisterViewModel
import java.util.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onCancel: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, _, _ ->
            viewModel.updateYearOfBirth(year.toString())
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            AppHeader(
                title = "Register",
                canNavigateBack = true,
                onNavigateBack = onCancel
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
            verticalArrangement = Arrangement.Center
        ) {
            // Username Field
            TextInput(
                value = uiState.username,
                onValueChange = viewModel::updateUsername,
                label = "Username",
                placeholder = "Enter username",
                errorText = uiState.usernameError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            PasswordInput(
                password = uiState.password,
                onPasswordChange = viewModel::updatePassword,
                passwordVisible = uiState.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() },
                label = "Password",
                placeholder = "Enter password",
                errorText = uiState.passwordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            PasswordInput(
                password = uiState.confirmPassword,
                onPasswordChange = viewModel::updateConfirmPassword,
                passwordVisible = uiState.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.toggleConfirmPasswordVisibility() },
                label = "Confirm Password",
                placeholder = "Re-enter password",
                errorText = uiState.confirmPasswordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            TextInput(
                value = uiState.email,
                onValueChange = viewModel::updateEmail,
                label = "Email",
                placeholder = "Enter email",
                errorText = uiState.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Selection
            GenderSelection(
                modifier = Modifier.fillMaxWidth(),
                selectedGender = uiState.selectedGender,
                onGenderSelected = viewModel::updateSelectedGender,
                errorText = uiState.genderError,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Year of Birth Picker
            TextInput(
                value = uiState.yearOfBirth,
                onValueChange = { },
                label = "Year of Birth",
                placeholder = "Select Year of birth",
                errorText = uiState.yearOfBirthError,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                trailingIcon = {
                    TextButton(
                        onClick = { datePickerDialog.show() },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Select")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Receive Updates Checkbox
            BooleanInput(
                label = "To receive updates via email",
                checked = uiState.receiveUpdates,
                onCheckedChange = viewModel::updateReceiveUpdates,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = {
                    viewModel.register(
                        onSuccess = {
                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Register")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading
            ) {
                Text("Cancel")
            }
        }
    }
}