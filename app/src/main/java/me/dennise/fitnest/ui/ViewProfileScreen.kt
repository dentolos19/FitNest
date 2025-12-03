package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.components.Avatar
import me.dennise.fitnest.ui.components.TitleDescription
import me.dennise.fitnest.ui.models.ViewProfileViewModel
import me.dennise.fitnest.ui.states.ViewProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(
    onNavigateBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    viewModel: ViewProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Profile",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ProfileDetailContent(
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun ProfileDetailContent(
    uiState: ViewProfileUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar()

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Profile Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                TitleDescription(
                    title = "Username",
                    description = uiState.username
                )

                Spacer(modifier = Modifier.height(12.dp))

                TitleDescription(
                    title = "Email",
                    description = uiState.email
                )

                Spacer(modifier = Modifier.height(12.dp))

                TitleDescription(
                    title = "Gender",
                    description = uiState.gender
                )

                Spacer(modifier = Modifier.height(12.dp))

                TitleDescription(
                    title = "Mobile",
                    description = uiState.mobile
                )

                Spacer(modifier = Modifier.height(12.dp))

                TitleDescription(
                    title = "Year of Birth",
                    description = uiState.yearOfBirth.toString()
                )

                Spacer(modifier = Modifier.height(12.dp))

                TitleDescription(
                    title = "Receive Updates",
                    description = if (uiState.receiveUpdates) "Yes" else "No"
                )
            }
        }
    }
}