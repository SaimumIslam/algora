package com.algora.app.feature.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algora.app.core.ui.theme.IBMPlexMono
import com.algora.app.core.ui.theme.SimColors
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

private const val MAX_CELLS = 12
private val initialCells = listOf(4, 8, 15, 16, 23)
private const val FLASH_DURATION_MS = 700L

private val cellSize = 64.dp
private val cellGap = 8.dp

private val BtnBlue = SimColors.Blue
private val BtnViolet = SimColors.Violet
private val BtnRed = SimColors.Red
private val BtnAmber = SimColors.Amber
private val BtnGrey = SimColors.Grey

@Composable
fun ArraySimulationSection() {
    val cells = remember { mutableStateListOf(*initialCells.toTypedArray()) }
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var statusMessage by remember { mutableStateOf("") }
    var valueInput by remember { mutableStateOf("") }
    var indexInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun flash(index: Int) {
        scope.launch {
            highlightedIndex = index
            delay(FLASH_DURATION_MS)
            highlightedIndex = null
        }
    }

    fun parsedValue(): Int? = valueInput.toIntOrNull()
    fun parsedIndex(): Int? = indexInput.toIntOrNull()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
            ) {
                ArrayCanvas(cells = cells, highlightedIndex = highlightedIndex)
            }

            if (statusMessage.isNotEmpty()) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                OutlinedTextField(
                    value = valueInput,
                    onValueChange = { valueInput = it.filter(Char::isDigit) },
                    label = { Text("Value") },
                    modifier = Modifier.weight(1f),
                )
                Box(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = indexInput,
                    onValueChange = { indexInput = it.filter(Char::isDigit) },
                    label = { Text("Index") },
                    modifier = Modifier.weight(1f),
                )
            }

            val opRow = @Composable { a: Triple<String, Color, () -> Unit>, b: Triple<String, Color, () -> Unit> ->
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Button(
                        onClick = a.third,
                        colors = ButtonDefaults.buttonColors(containerColor = a.second, contentColor = Color.White),
                        modifier = Modifier.weight(1f),
                    ) { Text(a.first) }
                    Box(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = b.third,
                        colors = ButtonDefaults.buttonColors(containerColor = b.second, contentColor = Color.White),
                        modifier = Modifier.weight(1f),
                    ) { Text(b.first) }
                }
            }

            opRow(
                Triple("Insert Head", BtnBlue) {
                    val v = parsedValue()
                    when {
                        v == null -> statusMessage = "Enter a value first"
                        cells.size >= MAX_CELLS -> statusMessage = "Array is full ($MAX_CELLS max)"
                        else -> {
                            cells.add(0, v)
                            statusMessage = "Inserted $v at head"
                            flash(0)
                        }
                    }
                },
                Triple("Insert Tail", BtnBlue) {
                    val v = parsedValue()
                    when {
                        v == null -> statusMessage = "Enter a value first"
                        cells.size >= MAX_CELLS -> statusMessage = "Array is full ($MAX_CELLS max)"
                        else -> {
                            cells.add(v)
                            statusMessage = "Inserted $v at tail"
                            flash(cells.lastIndex)
                        }
                    }
                },
            )

            opRow(
                Triple("Insert At", BtnViolet) {
                    val v = parsedValue()
                    val i = parsedIndex()
                    when {
                        v == null || i == null -> statusMessage = "Enter both value and index"
                        cells.size >= MAX_CELLS -> statusMessage = "Array is full ($MAX_CELLS max)"
                        i !in 0..cells.size -> statusMessage = "Index out of bounds"
                        else -> {
                            cells.add(i, v)
                            statusMessage = "Inserted $v at index $i"
                            flash(i)
                        }
                    }
                },
                Triple("Delete At", BtnRed) {
                    val i = parsedIndex()
                    when {
                        i == null -> statusMessage = "Enter an index first"
                        i !in cells.indices -> statusMessage = "Index out of bounds"
                        else -> {
                            val removed = cells[i]
                            statusMessage = "Deleting $removed at index $i…"
                            scope.launch {
                                highlightedIndex = i
                                delay(FLASH_DURATION_MS)
                                cells.removeAt(i)
                                highlightedIndex = null
                                statusMessage = "Deleted $removed"
                            }
                        }
                    }
                },
            )

            opRow(
                Triple("Search", BtnAmber) {
                    val v = parsedValue()
                    when {
                        v == null -> statusMessage = "Enter a value first"
                        else -> {
                            val foundIndex = cells.indexOf(v)
                            if (foundIndex >= 0) {
                                statusMessage = "Found $v at index $foundIndex"
                                flash(foundIndex)
                            } else {
                                statusMessage = "$v not found"
                            }
                        }
                    }
                },
                Triple("Reset", BtnGrey) {
                    cells.clear()
                    cells.addAll(initialCells)
                    highlightedIndex = null
                    statusMessage = "Reset to initial state"
                },
            )
        }
    }
}

@Composable
private fun ArrayCanvas(cells: List<Int>, highlightedIndex: Int?) {
    val textMeasurer = rememberTextMeasurer()
    val cellFill = MaterialTheme.colorScheme.surface
    val cellBorder = MaterialTheme.colorScheme.outline
    val highlightColor = MaterialTheme.colorScheme.tertiary
    val valueTextColor = MaterialTheme.colorScheme.onSurface
    val indexTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    val valueStyle = TextStyle(fontFamily = IBMPlexMono, fontWeight = FontWeight.Medium, fontSize = 18.sp, textAlign = TextAlign.Center)
    val indexStyle = TextStyle(fontFamily = IBMPlexMono, fontWeight = FontWeight.Normal, fontSize = 12.sp, textAlign = TextAlign.Center)

    val totalWidth = cellSize * cells.size + cellGap * (cells.size - 1).coerceAtLeast(0)

    Canvas(
        modifier = Modifier
            .width(totalWidth.coerceAtLeast(cellSize))
            .height(cellSize + 24.dp),
    ) {
        val cellPx = cellSize.toPx()
        val gapPx = cellGap.toPx()

        cells.forEachIndexed { index, value ->
            val left = index * (cellPx + gapPx)
            val isHighlighted = index == highlightedIndex

            drawRoundRect(
                color = if (isHighlighted) highlightColor.copy(alpha = 0.25f) else cellFill,
                topLeft = Offset(left, 0f),
                size = androidx.compose.ui.geometry.Size(cellPx, cellPx),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f),
            )
            drawRoundRect(
                color = if (isHighlighted) highlightColor else cellBorder,
                topLeft = Offset(left, 0f),
                size = androidx.compose.ui.geometry.Size(cellPx, cellPx),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = if (isHighlighted) 4f else 2f),
            )

            val valueLayout = textMeasurer.measure(value.toString(), valueStyle)
            drawText(
                valueLayout,
                color = valueTextColor,
                topLeft = Offset(left + (cellPx - valueLayout.size.width) / 2f, (cellPx - valueLayout.size.height) / 2f),
            )

            val indexLayout = textMeasurer.measure(index.toString(), indexStyle)
            drawText(
                indexLayout,
                color = indexTextColor,
                topLeft = Offset(left + (cellPx - indexLayout.size.width) / 2f, cellPx + 4f),
            )
        }
    }
}
