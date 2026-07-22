package com.algora.app.feature.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algora.app.core.ui.theme.AlgoraCodeStyle
import com.algora.app.core.ui.theme.SimColors
import kotlin.math.roundToInt

private enum class Gate(val label: String, val targets: List<Int>) {
    AND("AND", listOf(0, 0, 0, 1)),
    OR("OR", listOf(0, 1, 1, 1)),
    XOR("XOR", listOf(0, 1, 1, 0)),
}

private val inputs = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1)

private val PositiveColor = Color(0xFF16A34A)
private val NegativeColor = Color(0xFF6366F1)
private val BoundaryColor = Color(0xFFF59E0B)

// The classifier-gate solver from the Phase 3 scope, applied to The Perceptron: tune the two input
// weights and bias, watch the single neuron classify each logic-gate input, and see why XOR can't be
// separated by one straight line (the decision boundary).
@Composable
fun PerceptronSimulationSection() {
    var gate by remember { mutableStateOf(Gate.AND) }
    var w1 by remember { mutableStateOf(1f) }
    var w2 by remember { mutableStateOf(1f) }
    var bias by remember { mutableStateOf(-1.5f) }

    fun predict(x1: Int, x2: Int): Int = if (w1 * x1 + w2 * x2 + bias >= 0f) 1 else 0
    val correct = inputs.indices.count { predict(inputs[it].first, inputs[it].second) == gate.targets[it] }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // gate selector
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Gate.entries.forEach { g ->
                    val selected = g == gate
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(10.dp),
                            )
                            .clickable { gate = g }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            g.label,
                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(10.dp),
            ) {
                PerceptronCanvas(gate = gate, w1 = w1, w2 = w2, bias = bias)
            }

            // truth table
            Column(modifier = Modifier.padding(top = 14.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                    TableCell("x₁", weight = 1f, header = true)
                    TableCell("x₂", weight = 1f, header = true)
                    TableCell("target", weight = 1.4f, header = true)
                    TableCell("output", weight = 1.4f, header = true)
                }
                inputs.forEachIndexed { i, (x1, x2) ->
                    val out = predict(x1, x2)
                    val ok = out == gate.targets[i]
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                        TableCell(x1.toString(), weight = 1f)
                        TableCell(x2.toString(), weight = 1f)
                        TableCell(gate.targets[i].toString(), weight = 1.4f)
                        TableCell(
                            if (ok) "$out ✓" else "$out ✗",
                            weight = 1.4f,
                            color = if (ok) PositiveColor else SimColors.Red,
                        )
                    }
                }
            }

            Text(
                text = if (correct == 4) "All 4 correct — this gate is linearly separable." else "$correct / 4 correct.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (correct == 4) PositiveColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp),
            )
            if (gate == Gate.XOR) {
                Text(
                    "No single straight line can separate XOR — one perceptron will never reach 4/4. That gap is exactly why multi-layer networks were invented.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            WeightSlider("Weight w₁", w1, -2f..2f) { w1 = it }
            WeightSlider("Weight w₂", w2, -2f..2f) { w2 = it }
            WeightSlider("Bias b", bias, -3f..3f) { bias = it }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.TableCell(
    text: String,
    weight: Float,
    header: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = if (header) MaterialTheme.typography.bodySmall else AlgoraCodeStyle,
        color = if (header) MaterialTheme.colorScheme.onSurfaceVariant else color,
        fontWeight = if (header) FontWeight.SemiBold else FontWeight.Medium,
    )
}

@Composable
private fun WeightSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.padding(top = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(((value * 100).roundToInt() / 100f).toString(), style = AlgoraCodeStyle, color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = range)
    }
}

@Composable
private fun PerceptronCanvas(gate: Gate, w1: Float, w2: Float, bias: Float) {
    val gridColor = MaterialTheme.colorScheme.outline
    Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1.4f)) {
        val lo = -0.3f
        val hi = 1.3f
        fun px(x: Float): Float = (x - lo) / (hi - lo) * size.width
        fun py(y: Float): Float = size.height - (y - lo) / (hi - lo) * size.height

        // axes
        drawLine(gridColor, Offset(px(0f), 0f), Offset(px(0f), size.height), strokeWidth = 1f)
        drawLine(gridColor, Offset(0f, py(0f)), Offset(size.width, py(0f)), strokeWidth = 1f)

        // decision boundary: w1*x + w2*y + bias = 0
        if (kotlin.math.abs(w2) > 1e-3f) {
            val yAtLo = -(w1 * lo + bias) / w2
            val yAtHi = -(w1 * hi + bias) / w2
            drawLine(BoundaryColor, Offset(px(lo), py(yAtLo)), Offset(px(hi), py(yAtHi)), strokeWidth = 5f)
        } else if (kotlin.math.abs(w1) > 1e-3f) {
            val xAt = -bias / w1
            drawLine(BoundaryColor, Offset(px(xAt), 0f), Offset(px(xAt), size.height), strokeWidth = 5f)
        }

        // gate input points colored by target class
        inputs.forEachIndexed { i, (x1, x2) ->
            val color = if (gate.targets[i] == 1) PositiveColor else NegativeColor
            drawCircle(color, radius = 12f, center = Offset(px(x1.toFloat()), py(x2.toFloat())))
        }
    }
}
