package me.dennise.fitnest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.dennise.fitnest.data.WorkoutEnjoyment
import me.dennise.fitnest.ui.theme.AppTheme

@Composable
fun EnjoymentSlider(
    modifier: Modifier = Modifier,
    enjoymentText: String,
    enjoymentIndex: Int,
    onEnjoymentChange: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = enjoymentText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = enjoymentIndex.toFloat(),
            onValueChange = { onEnjoymentChange(it.toInt()) },
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${WorkoutEnjoyment.entries.first().emoji} ${WorkoutEnjoyment.entries.first().label}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "${WorkoutEnjoyment.entries.last().emoji} ${WorkoutEnjoyment.entries.last().label}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnjoymentSliderPreview() {
    AppTheme {
        EnjoymentSlider(
            enjoymentText = "Energizing ⚡",
            enjoymentIndex = 0,
            onEnjoymentChange = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}