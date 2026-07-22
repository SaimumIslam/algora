package com.algora.app.feature.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algora.app.core.ui.theme.SimColors

// ── DP tabulation grid ───────────────────────────────────────────────────────
// Fills a DP table cell-by-cell (one PlaybackTransport step each), then, for 2-D problems, walks the
// traceback path. Each frame snapshots the values written so far + the active/traced cells.

private class DpFrame(
    val values: Map<Int, String>,   // cellKey -> written value
    val active: Int?,               // cell being written or traced now
    val traced: Set<Int>,           // traceback cells revealed so far
    val status: String,
)

private class DpConfig(
    val rows: Int,
    val cols: Int,
    val rowHeader: (Int) -> String,
    val colHeader: (Int) -> String,
    val corner: String,
    val intro: String,
    val build: () -> List<DpFrame>,
)

private val ActiveCell = Color(0xFFFACC15)
private val FilledCell = Color(0xFF3B82F6)
private val TracedCell = SimColors.Green

private class DpBuilder(val rows: Int, val cols: Int) {
    val frames = mutableListOf<DpFrame>()
    val values = LinkedHashMap<Int, String>()
    fun key(r: Int, c: Int) = r * cols + c
    fun fill(r: Int, c: Int, value: String, status: String) {
        values[key(r, c)] = value
        frames.add(DpFrame(values.toMap(), key(r, c), emptySet(), status))
    }
    fun trace(cells: List<Int>, statusFor: (Int) -> String) {
        val traced = mutableSetOf<Int>()
        cells.forEach { cell ->
            traced.add(cell)
            frames.add(DpFrame(values.toMap(), cell, traced.toSet(), statusFor(cell)))
        }
    }
}

// dp[i] = dp[i-1] + dp[i-2], filled left to right. 1-D, no traceback.
private fun fibonacciDpFrames(): List<DpFrame> {
    val n = 9
    val b = DpBuilder(rows = 1, cols = n + 1)
    val dp = LongArray(n + 1)
    for (i in 0..n) {
        dp[i] = if (i < 2) i.toLong() else dp[i - 1] + dp[i - 2]
        val status = if (i < 2) "dp[$i] = $i  (base case)"
        else "dp[$i] = dp[${i - 1}] + dp[${i - 2}] = ${dp[i]}"
        b.fill(0, i, dp[i].toString(), status)
    }
    return b.frames
}

private fun editDistanceFrames(): List<DpFrame> {
    val a = "cat"
    val bWord = "cut"
    val rows = a.length + 1
    val cols = bWord.length + 1
    val bld = DpBuilder(rows, cols)
    val dp = Array(rows) { IntArray(cols) }
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val status: String
            dp[i][j] = when {
                i == 0 -> { status = "dp[0][$j] = $j  (turn ε into first $j chars)"; j }
                j == 0 -> { status = "dp[$i][0] = $i  (delete $i chars)"; i }
                a[i - 1] == bWord[j - 1] -> {
                    status = "'${a[i - 1]}' == '${bWord[j - 1]}' → dp[${i - 1}][${j - 1}] = ${dp[i - 1][j - 1]}"
                    dp[i - 1][j - 1]
                }
                else -> {
                    val v = 1 + minOf(dp[i - 1][j - 1], dp[i - 1][j], dp[i][j - 1])
                    status = "'${a[i - 1]}' ≠ '${bWord[j - 1]}' → 1 + min(diag, up, left) = $v"
                    v
                }
            }
            bld.fill(i, j, dp[i][j].toString(), status)
        }
    }
    // traceback from bottom-right
    var i = rows - 1
    var j = cols - 1
    val path = mutableListOf(bld.key(i, j))
    while (i > 0 || j > 0) {
        val match = i > 0 && j > 0 && a[i - 1] == bWord[j - 1]
        when {
            i > 0 && j > 0 && (match || dp[i][j] == dp[i - 1][j - 1] + 1) -> { i--; j-- }
            i > 0 && dp[i][j] == dp[i - 1][j] + 1 -> i--
            else -> j--
        }
        path.add(bld.key(i, j))
    }
    bld.trace(path.reversed()) { "Traceback — the edit path. Distance = ${dp[rows - 1][cols - 1]}." }
    return bld.frames
}

private fun lcsFrames(): List<DpFrame> {
    val a = "abcbd"
    val bWord = "acbd"
    val rows = a.length + 1
    val cols = bWord.length + 1
    val bld = DpBuilder(rows, cols)
    val dp = Array(rows) { IntArray(cols) }
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val status: String
            dp[i][j] = when {
                i == 0 || j == 0 -> { status = "dp[$i][$j] = 0  (empty prefix)"; 0 }
                a[i - 1] == bWord[j - 1] -> {
                    status = "'${a[i - 1]}' matches → dp[${i - 1}][${j - 1}] + 1 = ${dp[i - 1][j - 1] + 1}"
                    dp[i - 1][j - 1] + 1
                }
                else -> {
                    val v = maxOf(dp[i - 1][j], dp[i][j - 1])
                    status = "no match → max(up, left) = $v"
                    v
                }
            }
            bld.fill(i, j, dp[i][j].toString(), status)
        }
    }
    var i = rows - 1
    var j = cols - 1
    val path = mutableListOf(bld.key(i, j))
    while (i > 0 && j > 0) {
        when {
            a[i - 1] == bWord[j - 1] -> { i--; j-- }
            dp[i - 1][j] >= dp[i][j - 1] -> i--
            else -> j--
        }
        path.add(bld.key(i, j))
    }
    bld.trace(path.reversed()) { "Traceback — matched characters. LCS length = ${dp[rows - 1][cols - 1]}." }
    return bld.frames
}

private val dpConfigs = mapOf(
    "fibonacci_dp" to DpConfig(
        rows = 1, cols = 10,
        rowHeader = { "dp" }, colHeader = { it.toString() }, corner = "i",
        intro = "Bottom-up Fibonacci: each cell is the sum of the two before it — computed once, left to right. No recursion, no repeated work.",
        build = ::fibonacciDpFrames,
    ),
    "edit_distance" to DpConfig(
        rows = 4, cols = 4,
        rowHeader = { if (it == 0) "ε" else "cat"[it - 1].toString() },
        colHeader = { if (it == 0) "ε" else "cut"[it - 1].toString() },
        corner = "",
        intro = "Edit distance between \"cat\" and \"cut\". Each cell is the cheapest way to turn one prefix into the other; the traceback shows the actual edits.",
        build = ::editDistanceFrames,
    ),
    "longest_common_subsequence" to DpConfig(
        rows = 6, cols = 5,
        rowHeader = { if (it == 0) "ε" else "abcbd"[it - 1].toString() },
        colHeader = { if (it == 0) "ε" else "acbd"[it - 1].toString() },
        corner = "",
        intro = "Longest common subsequence of \"abcbd\" and \"acbd\". On a match the diagonal grows; otherwise carry the best neighbour. Traceback recovers the subsequence.",
        build = ::lcsFrames,
    ),
)

private fun dpConfigFor(topicId: String): DpConfig =
    dpConfigs[topicId] ?: dpConfigs.getValue("fibonacci_dp")

@Composable
fun DpGridSection(topicId: String) {
    val config = remember(topicId) { dpConfigFor(topicId) }
    val frames = remember(config) { config.build() }
    val playback = rememberPlaybackState(key = config, stepCount = frames.size)
    val frame = frames[playback.index.coerceIn(0, frames.lastIndex)]

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                config.intro,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            DpGrid(config = config, frame = frame, modifier = Modifier.padding(top = 14.dp))

            Text(
                frame.status,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                DpLegend(ActiveCell, "Current")
                DpLegend(FilledCell, "Filled")
                if (config.rows > 1) DpLegend(TracedCell, "Traceback")
            }

            PlaybackTransport(playback)
        }
    }
}

@Composable
private fun DpLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 5.dp),
        )
    }
}

@Composable
private fun DpGrid(config: DpConfig, frame: DpFrame, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Header row: corner + column headers.
        Row(modifier = Modifier.fillMaxWidth()) {
            HeaderCell(config.corner)
            for (c in 0 until config.cols) HeaderCell(config.colHeader(c))
        }
        for (r in 0 until config.rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                HeaderCell(config.rowHeader(r))
                for (c in 0 until config.cols) {
                    val key = r * config.cols + c
                    DataCell(
                        value = frame.values[key],
                        color = when {
                            frame.active == key -> ActiveCell
                            key in frame.traced -> TracedCell
                            frame.values.containsKey(key) -> FilledCell.copy(alpha = 0.30f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HeaderCell(text: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .padding(1.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.DataCell(value: String?, color: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .padding(1.dp)
            .background(color, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            value ?: "",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
