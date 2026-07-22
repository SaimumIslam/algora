package com.algora.app.feature.analysis.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.ln
import kotlin.math.roundToInt

private class Curve(val label: String, val color: Color, val fn: (Double) -> Double)

private val curves = listOf(
    Curve("O(1)", Color(0xFF3B82F6)) { 1.0 },
    Curve("O(log n)", Color(0xFF10B981)) { n -> ln(n + 1) },
    Curve("O(n)", Color(0xFFF59E0B)) { n -> n },
    Curve("O(n log n)", Color(0xFF8B5CF6)) { n -> n * ln(n + 1) },
    Curve("O(n²)", Color(0xFFEF4444)) { n -> n * n },
)

@Composable
fun GrowthCurveChartTool() {
    var maxN by remember { mutableStateOf(50f) }
    val maxNInt = maxN.toInt().coerceAtLeast(1)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "How operation count grows with input size n, across the complexity classes you'll see throughout this app. Y-axis is log-scaled so all five curves stay visible at once.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(modifier = Modifier.padding(top = 14.dp, bottom = 6.dp)) {
                Text(
                    "Max n = $maxNInt",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Slider(value = maxN, onValueChange = { maxN = it }, valueRange = 10f..100f, steps = 8)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(6.dp),
            ) {
                GrowthCanvas(maxNInt = maxNInt)
            }

            Column(modifier = Modifier.padding(top = 14.dp)) {
                curves.forEach { curve ->
                    val value = curve.fn(maxNInt.toDouble())
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(curve.color, CircleShape),
                        )
                        Text(
                            curve.label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp).weight(1f),
                        )
                        Text(
                            "≈ ${value.roundToInt()} ops",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GrowthCanvas(maxNInt: Int) {
    val gridColor = MaterialTheme.colorScheme.outline

    Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        val maxValue = curves.last().fn(maxNInt.toDouble())
        val logMax = ln(maxValue + 1)
        val padding = 8f

        fun xOf(n: Int): Float = padding + (n.toFloat() / maxNInt) * (size.width - 2 * padding)
        fun yOf(value: Double): Float {
            val normalized = (ln(value + 1) / logMax).toFloat().coerceIn(0f, 1f)
            return size.height - padding - normalized * (size.height - 2 * padding)
        }

        drawLine(gridColor, Offset(padding, size.height - padding), Offset(size.width - padding, size.height - padding), strokeWidth = 2f)
        drawLine(gridColor, Offset(padding, padding), Offset(padding, size.height - padding), strokeWidth = 2f)

        val sampleStep = (maxNInt / 60).coerceAtLeast(1)
        curves.forEach { curve ->
            val path = Path()
            var first = true
            var n = 1
            while (n <= maxNInt) {
                val x = xOf(n)
                val y = yOf(curve.fn(n.toDouble()))
                if (first) {
                    path.moveTo(x, y)
                    first = false
                } else {
                    path.lineTo(x, y)
                }
                n += sampleStep
            }
            drawPath(path, color = curve.color, style = Stroke(width = 3f))
        }
    }
}
