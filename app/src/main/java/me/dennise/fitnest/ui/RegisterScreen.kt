package me.dennise.fitnest.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.BooleanInput
import me.dennise.fitnest.ui.components.GenderSelection
import me.dennise.fitnest.ui.components.HeadBar
import me.dennise.fitnest.ui.components.PasswordInput
import me.dennise.fitnest.ui.components.TextInput
import me.dennise.fitnest.ui.models.RegisterViewModel
import me.dennise.fitnest.ui.theme.AppTheme
import java.util.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onCancel: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
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
            HeadBar(
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User ID Field
            TextInput(
                value = viewModel.state.userName,
                onValueChange = viewModel::updateUserName,
                label = "User ID",
                placeholder = "Enter user ID",
                errorText = viewModel.state.userNameError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Password Field
            PasswordInput(
                password = viewModel.state.password,
                onPasswordChange = viewModel::updatePassword,
                passwordVisible = viewModel.state.passwordVisible,
                onPasswordVisibilityChange = { viewModel.togglePasswordVisibility() },
                label = "Password",
                placeholder = "Enter password",
                errorText = viewModel.state.passwordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Confirm Password Field
            PasswordInput(
                password = viewModel.state.confirmPassword,
                onPasswordChange = viewModel::updateConfirmPassword,
                passwordVisible = viewModel.state.confirmPasswordVisible,
                onPasswordVisibilityChange = { viewModel.toggleConfirmPasswordVisibility() },
                label = "Confirm Password",
                placeholder = "Re-enter password",
                errorText = viewModel.state.confirmPasswordError,
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Email Field
            TextInput(
                value = viewModel.state.email,
                onValueChange = viewModel::updateEmail,
                label = "Email",
                placeholder = "Enter email",
                errorText = viewModel.state.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Receive Updates Checkbox
            BooleanInput(
                label = "To receive updates via email",
                checked = viewModel.state.receiveUpdates,
                onCheckedChange = viewModel::updateReceiveUpdates,
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Mobile Number Field
            TextInput(
                value = viewModel.state.mobileNumber,
                onValueChange = viewModel::updateMobileNumber,
                label = "Mobile Number",
                placeholder = "Enter mobile number",
                errorText = viewModel.state.mobileNumberError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            )

            // Gender Selection
            GenderSelection(
                modifier = Modifier.fillMaxWidth(),
                selectedGender = viewModel.state.selectedGender,
                onGenderSelected = viewModel::updateSelectedGender,
                errorText = viewModel.state.genderError,
                enabled = !viewModel.state.isLoading
            )

            // Year of Birth Picker
            TextInput(
                value = viewModel.state.yearOfBirth,
                onValueChange = { },
                label = "Year of Birth",
                placeholder = "Select Year of birth",
                errorText = viewModel.state.yearOfBirthError,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading,
                trailingIcon = {
                    TextButton(
                        onClick = { datePickerDialog.show() },
                        enabled = !viewModel.state.isLoading
                    ) {
                        Text("Select")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.state.isLoading
            ) {
                if (viewModel.state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Register")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterPreview() {
    AppTheme {
        RegisterScreen()
    }
}