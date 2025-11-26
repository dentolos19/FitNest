package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.data.WorkoutCategory
import me.dennise.fitnest.getWorkoutCategoryIcon
import me.dennise.fitnest.ui.components.EnjoymentSlider
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.components.TextInput
import me.dennise.fitnest.ui.models.WorkoutAddViewModel
import me.dennise.fitnest.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutAddScreen(
    onNavigateBack: () -> Unit = {},
    onWorkoutAdded: () -> Unit = {},
    viewModel: WorkoutAddViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Cancel confirmation dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel?") },
            text = { Text("Are you sure you want to discard this workout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateDate(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateTime(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "New Workout",
                canNavigateBack = true,
                onNavigateBack = { showCancelDialog = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Workout Icon
            Icon(
                imageVector = getWorkoutCategoryIcon(uiState.category),
                contentDescription = uiState.category,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Field
            TextInput(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = "Name",
                placeholder = "Enter workout name",
                errorText = uiState.nameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Workout Category Dropdown
            var categoryExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Workout Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    WorkoutCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName) },
                            onClick = {
                                viewModel.updateCategory(category.displayName)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration Field (Optional)
            TextInput(
                value = uiState.duration,
                onValueChange = { viewModel.updateDuration(it) },
                label = "Duration (minutes)",
                placeholder = "Optional",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date Field (Optional)
            TextInput(
                value = uiState.date,
                onValueChange = {},
                label = "Date",
                placeholder = "Optional",
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Field (Optional)
            TextInput(
                value = uiState.time,
                onValueChange = {},
                label = "Time",
                placeholder = "Optional",
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Select Time"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Comments Field (Optional)
            TextInput(
                value = uiState.comments,
                onValueChange = { viewModel.updateComments(it) },
                label = "Comments",
                placeholder = "Enter comments",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(32.dp))

            EnjoymentSlider(
                enjoymentText = uiState.enjoyment,
                enjoymentIndex = uiState.enjoymentIndex,
                onEnjoymentChange = { viewModel.updateEnjoyment(it) },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (viewModel.validateAndSave()) {
                        onWorkoutAdded()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WorkoutAddPreview() {
    AppTheme {
        WorkoutAddScreen()
    }
}