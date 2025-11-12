package me.dennise.fitnest.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.theme.AppTheme
import java.util.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onCancel: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var receiveUpdates by remember { mutableStateOf(false) }
    var yearOfBirth by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, _, _ ->
            yearOfBirth = year.toString()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // User name field
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                placeholder = { Text("Enter user name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Confirm password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Enter password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("Enter email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Gender selection
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Gender",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val genderOptions = listOf("Male", "Female", "Non-Binary", "Prefer not to say")

                genderOptions.forEach { gender ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedGender == gender,
                            onClick = { selectedGender = gender }
                        )
                        Text(
                            text = gender,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Mobile number field
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = {
                    // Only allow digits
                    if (it.all { char -> char.isDigit() }) {
                        mobileNumber = it
                    }
                },
                label = { Text("Mobile Number") },
                placeholder = { Text("Enter mobile number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // To receive updates checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = receiveUpdates,
                    onCheckedChange = { receiveUpdates = it }
                )
                Text(
                    text = "To receive updates via email",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Year of birth date picker
            OutlinedTextField(
                value = yearOfBirth,
                onValueChange = { },
                label = { Text("Year of Birth") },
                placeholder = { Text("Select Year of birth") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { datePickerDialog.show() }) {
                        Text("Select")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Register button
            Button(
                onClick = {
                    // Validate all required fields
                    when {
                        userName.isBlank() -> {
                            Toast.makeText(context, "Please enter user name", Toast.LENGTH_SHORT).show()
                        }

                        password.isBlank() -> {
                            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                        }

                        confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Please confirm password", Toast.LENGTH_SHORT).show()
                        }

                        password != confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }

                        email.isBlank() -> {
                            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                        }

                        selectedGender.isBlank() -> {
                            Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show()
                        }

                        mobileNumber.isBlank() -> {
                            Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show()
                        }

                        yearOfBirth.isBlank() -> {
                            Toast.makeText(context, "Please select year of birth", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            // All validations passed, save details to database
                            viewModel.registerUser(
                                username = userName,
                                password = password,
                                email = email,
                                gender = selectedGender,
                                mobile = mobileNumber,
                                yearOfBirth = yearOfBirth.toInt(),
                                receiveUpdates = receiveUpdates,
                                onSuccess = {
                                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess()
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Registration")
            }

            // Cancel button
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel Registration")
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
