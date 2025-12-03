package me.dennise.fitnest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.dennise.fitnest.R
import me.dennise.fitnest.ui.theme.AppTheme

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    contentDescription: String = "User Avatar"
) {
    Image(
        painter = painterResource(id = R.drawable.avatar),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    AppTheme {
        Avatar()
    }
}