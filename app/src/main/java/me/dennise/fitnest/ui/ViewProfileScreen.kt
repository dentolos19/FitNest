package me.dennise.fitnest.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.dennise.fitnest.R
import me.dennise.fitnest.ui.components.AppHeader
import me.dennise.fitnest.ui.models.ViewProfileViewModel
import me.dennise.fitnest.ui.states.ViewProfileUiState
import me.dennise.fitnest.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(
    onNavigateBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    viewModel: ViewProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "View Profile",
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
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

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

                DetailRow(
                    label = "Username",
                    value = uiState.username
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Email",
                    value = uiState.email
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Gender",
                    value = uiState.gender
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Mobile",
                    value = uiState.mobile
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Year of Birth",
                    value = uiState.yearOfBirth.toString()
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Receive Updates",
                    value = if (uiState.receiveUpdates) "Yes" else "No"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewProfileScreenPreview() {
    AppTheme {
        ProfileDetailContent(
            uiState = ViewProfileUiState(
                username = "dennise",
                email = "dennise@example.com",
                gender = "Female",
                mobile = "1234567890",
                yearOfBirth = 1990,
                receiveUpdates = true
            )
        )
    }
}
