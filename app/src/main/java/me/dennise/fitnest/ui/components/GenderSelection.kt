package me.dennise.fitnest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelection(
    modifier: Modifier = Modifier,
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    enabled: Boolean = true,
    errorText: String? = null,
) {
    val genderOptions = listOf("Male", "Female", "Non-Binary", "Prefer Not To Say")

    Column(modifier = modifier) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        genderOptions.forEach { gender ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = gender == selectedGender,
                    onClick = { onGenderSelected(gender) },
                    enabled = enabled
                )
                Text(
                    text = gender,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        ErrorText(text = errorText)
    }
}