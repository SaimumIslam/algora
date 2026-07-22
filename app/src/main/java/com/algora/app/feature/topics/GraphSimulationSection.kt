package com.algora.app.feature.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algora.app.core.ui.theme.SimColors
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val demoNodes = listOf("A", "B", "C", "D", "E", "F")
private val demoEdges = listOf(
    "A" to "B", "A" to "C", "B" to "D", "C" to "D", "E" to "A", "E" to "F", "B" to "F",
)
private const val TRAVERSAL_STEP_MS = 600L
private const val MAX_NODES = 10

private enum class NodeState { IDLE, CURRENT, VISITED, QUEUE }

private val NodeStateColors = mapOf(
    NodeState.IDLE to Color(0xFF7C3AED),
    NodeState.CURRENT to Color(0xFFFACC15),
    NodeState.VISITED to Color(0xFFF97316),
    NodeState.QUEUE to Color(0xFF3B82F6),
)

private fun buildAdjacency(nodes: List<String>, edges: List<Pair<String, String>>): Map<String, List<String>> {
    val adjacency = nodes.associateWith { mutableListOf<String>() }
    for ((a, b) in edges) {
        adjacency[a]?.add(b)
        adjacency[b]?.add(a)
    }
    return adjacency.mapValues { it.value.sorted() }
}

private fun bfsSnapshots(nodes: List<String>, adjacency: Map<String, List<String>>, start: String): List<Pair<Map<String, NodeState>, String>> {
    val state = nodes.associateWith { NodeState.IDLE }.toMutableMap()
    val snapshots = mutableListOf<Pair<Map<String, NodeState>, String>>()
    fun snap(status: String) = snapshots.add(state.toMap() to status)

    val visited = mutableSetOf(start)
    state[start] = NodeState.QUEUE
    snap("Enqueue $start")

    val queue = ArrayDeque(listOf(start))
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        state[node] = NodeState.CURRENT
        snap("Visit $node")
        for (neighbor in adjacency[node].orEmpty()) {
            if (visited.add(neighbor)) {
                state[neighbor] = NodeState.QUEUE
                snap("Enqueue $neighbor")
                queue.addLast(neighbor)
            }
        }
        state[node] = NodeState.VISITED
    }
    snap("Complete")
    return snapshots
}

private fun dfsSnapshots(nodes: List<String>, adjacency: Map<String, List<String>>, start: String): List<Pair<Map<String, NodeState>, String>> {
    val state = nodes.associateWith { NodeState.IDLE }.toMutableMap()
    val snapshots = mutableListOf<Pair<Map<String, NodeState>, String>>()
    fun snap(status: String) = snapshots.add(state.toMap() to status)

    val visited = mutableSetOf<String>()
    val stack = ArrayDeque(listOf(start))
    state[start] = NodeState.QUEUE
    snap("Push $start")

    while (stack.isNotEmpty()) {
        val node = stack.removeLast()
        if (!visited.add(node)) continue
        state[node] = NodeState.CURRENT
        snap("Visit $node")
        state[node] = NodeState.VISITED
        for (neighbor in adjacency[node].orEmpty().filter { it !in visited }.reversed()) {
            stack.addLast(neighbor)
            state[neighbor] = NodeState.QUEUE
            snap("Push $neighbor")
        }
    }
    snap("Complete")
    return snapshots
}

@Composable
fun GraphSimulationSection() {
    var nodes by remember { mutableStateOf(demoNodes) }
    var edges by remember { mutableStateOf(demoEdges) }
    var nodeState by remember { mutableStateOf(nodes.associateWith { NodeState.IDLE }) }
    var status by remember { mutableStateOf("Idle") }
    var fromNode by remember { mutableStateOf(nodes.first()) }
    var toNode by remember { mutableStateOf(nodes[1]) }
    var startNode by remember { mutableStateOf(nodes.first()) }
    var traversalJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    fun cancelTraversal() {
        traversalJob?.cancel()
        traversalJob = null
    }

    fun runTraversal(kind: String) {
        cancelTraversal()
        val adjacency = buildAdjacency(nodes, edges)
        val snapshots = if (kind == "BFS") bfsSnapshots(nodes, adjacency, startNode) else dfsSnapshots(nodes, adjacency, startNode)
        traversalJob = scope.launch {
            for ((snapState, snapStatus) in snapshots) {
                nodeState = snapState
                status = snapStatus
                delay(TRAVERSAL_STEP_MS)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                GraphStat(label = "Nodes", value = nodes.size.toString(), modifier = Modifier.weight(1f))
                GraphStat(label = "Edges", value = edges.size.toString(), modifier = Modifier.weight(1f))
                GraphStat(label = "Status", value = status, modifier = Modifier.weight(1f), valueColor = MaterialTheme.colorScheme.primary)
            }

            GraphCanvas(nodes = nodes, edges = edges, nodeState = nodeState)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                val legend = listOf(
                    NodeState.IDLE to "Idle",
                    NodeState.CURRENT to "Current",
                    NodeState.VISITED to "Visited",
                    NodeState.QUEUE to "Queue",
                )
                legend.forEach { (state, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(NodeStateColors.getValue(state), CircleShape),
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 5.dp),
                        )
                    }
                }
            }

            Button(
                onClick = {
                    cancelTraversal()
                    if (nodes.size < MAX_NODES) {
                        val nextLetter = ('A' + nodes.size).toString()
                        nodes = nodes + nextLetter
                        nodeState = nodes.associateWith { NodeState.IDLE }
                        status = "Idle"
                    } else {
                        status = "Graph is full ($MAX_NODES max)"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SimColors.Blue, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp),
            ) { Text("+ Add Node") }

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 9.dp)) {
                NodeDropdown(label = "From", selected = fromNode, options = nodes, onSelected = { fromNode = it }, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.width(9.dp))
                NodeDropdown(label = "To", selected = toNode, options = nodes, onSelected = { toNode = it }, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.width(9.dp))
                Button(
                    onClick = {
                        cancelTraversal()
                        status = when {
                            fromNode == toNode -> "Pick two different nodes"
                            edges.any { (a, b) -> (a == fromNode && b == toNode) || (a == toNode && b == fromNode) } -> "Edge exists"
                            else -> {
                                edges = edges + (fromNode to toNode)
                                "Linked"
                            }
                        }
                        nodeState = nodes.associateWith { NodeState.IDLE }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SimColors.Violet, contentColor = Color.White),
                ) { Text("Link") }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                NodeDropdown(label = "Start", selected = startNode, options = nodes, onSelected = { startNode = it }, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.width(9.dp))
                Button(
                    onClick = { runTraversal("BFS") },
                    colors = ButtonDefaults.buttonColors(containerColor = SimColors.Green, contentColor = Color.White),
                ) { Text("▸ BFS") }
                Box(modifier = Modifier.width(9.dp))
                Button(
                    onClick = { runTraversal("DFS") },
                    colors = ButtonDefaults.buttonColors(containerColor = SimColors.Amber, contentColor = Color.White),
                ) { Text("▸ DFS") }
                Box(modifier = Modifier.width(9.dp))
                Button(
                    onClick = {
                        cancelTraversal()
                        nodeState = nodes.associateWith { NodeState.IDLE }
                        status = "Idle"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SimColors.Grey, contentColor = Color.White),
                ) { Text("↺") }
            }
        }
    }
}

@Composable
private fun GraphStat(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun NodeDropdown(label: String, selected: String, options: List<String>, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(selected, style = MaterialTheme.typography.bodyLarge)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onSelected(option); expanded = false })
            }
        }
    }
}

@Composable
private fun GraphCanvas(nodes: List<String>, edges: List<Pair<String, String>>, nodeState: Map<String, NodeState>) {
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(6.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val rx = size.width / 2f - 40f
            val ry = size.height / 2f - 34f
            val n = nodes.size.coerceAtLeast(1)

            fun positionOf(index: Int): Offset {
                val angle = -Math.PI / 2 + index * 2 * Math.PI / n
                return Offset(cx + rx * cos(angle).toFloat(), cy + ry * sin(angle).toFloat())
            }

            val positions = nodes.indices.associateWith { positionOf(it) }
            val indexByNode = nodes.withIndex().associate { (i, id) -> id to i }

            edges.forEach { (a, b) ->
                val pa = indexByNode[a]?.let { positions[it] }
                val pb = indexByNode[b]?.let { positions[it] }
                if (pa != null && pb != null) {
                    drawLine(color = Color(0xFFCBD0DA), start = pa, end = pb, strokeWidth = 2f)
                }
            }

            nodes.forEachIndexed { index, id ->
                val center = positions.getValue(index)
                val color = NodeStateColors.getValue(nodeState[id] ?: NodeState.IDLE)
                drawCircle(color = color, radius = 17f, center = center)
                drawCircle(color = Color.White, radius = 17f, center = center, style = Stroke(width = 2.5f))

                val layout = textMeasurer.measure(id, labelStyle)
                drawText(
                    layout,
                    topLeft = Offset(center.x - layout.size.width / 2f, center.y - layout.size.height / 2f),
                )
            }
        }
    }
}
