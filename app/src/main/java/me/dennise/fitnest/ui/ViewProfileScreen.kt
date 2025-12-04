package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.components.Avatar
import me.dennise.fitnest.ui.components.LabelValue
import me.dennise.fitnest.ui.models.ViewProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    viewModel: ViewProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Profile",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    onNavigateToEdit()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Profile"
                                    )
                                }
                            )
                        }
                    }
                }
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "User Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LabelValue(label = "Username", value = uiState.username)
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValue(label = "Email", value = uiState.email)
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValue(label = "Gender", value = uiState.gender)
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValue(label = "Mobile", value = uiState.mobile)
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValue(label = "Year of Birth", value = uiState.yearOfBirth.toString())
                    Spacer(modifier = Modifier.height(8.dp))
                    LabelValue(label = "Receive Updates", value = if (uiState.receiveUpdates) "Yes" else "No")
                }
            }
        }
    }
}
