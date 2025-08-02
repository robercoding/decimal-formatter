package dev.robercoding.decimal.formatter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Header(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}