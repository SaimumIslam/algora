package com.algora.app.feature.topics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Shared button-row layout for the data-structure sim widgets (LinkedList/Stack/Queue) — one row
// of equally-weighted action buttons colored via SimColors.
@Composable
fun SimButtonRow(buttons: List<Triple<String, Color, () -> Unit>>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        buttons.forEachIndexed { index, (label, color, onClick) ->
            if (index > 0) Box(modifier = Modifier.width(8.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
                modifier = Modifier.weight(1f),
            ) { Text(label) }
        }
    }
}
