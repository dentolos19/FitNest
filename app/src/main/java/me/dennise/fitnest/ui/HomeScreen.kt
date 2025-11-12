package me.dennise.fitnest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.dennise.fitnest.ui.theme.AppTheme

@Composable
fun HomeScreen() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Home Screen")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview() {
    AppTheme {
        HomeScreen()
    }
}