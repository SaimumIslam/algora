package com.algora.app.feature.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algora.app.core.data.model.Topic
import com.algora.app.core.data.progress.ProgressRepository
import com.algora.app.core.data.progress.progressDataStore
import com.algora.app.core.data.settings.SettingsRepository
import com.algora.app.core.data.settings.settingsDataStore
import com.algora.app.core.nav.AppMode
import com.algora.app.core.ui.components.resolveIcon
import com.algora.app.core.ui.theme.SpaceGrotesk
import com.algora.app.feature.algorithms.AlgorithmsTopics
import com.algora.app.feature.analysis.AnalysisTopics
import com.algora.app.feature.datastructures.DataStructuresTopics
import com.algora.app.feature.deeplearning.DeepLearningTopics
import com.algora.app.feature.interviewprep.InterviewPrepTopics
import com.algora.app.feature.machinelearning.MachineLearningTopics
import com.algora.app.feature.nlp.NlpTopics
import com.algora.app.feature.reinforcementlearning.ReinforcementLearningTopics

// Category-group definition for the progress dashboard. Titles/icons/colors come straight from
// docs/design/Algora.dc.html's isProgress block (gcols / gicons maps).
private data class ProgressGroup(
    val title: String,
    val iconName: String,
    val color: Color,
    val topics: List<Topic>,
)

private val dsaGroups = listOf(
    ProgressGroup("Data Structures", "stack", Color(0xFF10B981), DataStructuresTopics.topics),
    ProgressGroup("Algorithms", "chip", Color(0xFF3B82F6), AlgorithmsTopics.topics),
    ProgressGroup("Interview Prep", "help", Color(0xFFF59E0B), InterviewPrepTopics.topics),
    ProgressGroup("Analysis", "trend", Color(0xFF8B5CF6), AnalysisTopics.topics),
)

private val aiGroups = listOf(
    ProgressGroup("Machine Learning", "robot", Color(0xFF6366F1), MachineLearningTopics.topics),
    ProgressGroup("Deep Learning", "network", Color(0xFFEC4899), DeepLearningTopics.topics),
    ProgressGroup("NLP", "globe", Color(0xFF14B8A6), NlpTopics.topics),
    ProgressGroup("Reinforcement Learning", "game", Color(0xFFF97316), ReinforcementLearningTopics.topics),
)

// Ported from docs/design/Algora.dc.html's isProgress block: an overall completion ring + streak,
// then one card per top-level category with a progress bar. Grouping follows the active mode's four
// categories (DSA vs AI), matching the mock's mode-keyed progress view.
@Composable
fun ProgressScreen(mode: AppMode, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val progressRepo = remember { ProgressRepository(context.progressDataStore) }
    val settings = remember { SettingsRepository(context.settingsDataStore) }
    val completedIds by progressRepo.completedTopicIds.collectAsState(initial = emptySet())
    val streak by settings.streak.collectAsState(initial = 0)

    val groups = if (mode == AppMode.DSA) dsaGroups else aiGroups
    val overallTotal = groups.sumOf { it.topics.size }
    val overallDone = groups.sumOf { g -> g.topics.count { it.id in completedIds } }
    val overallPct = if (overallTotal == 0) 0 else (overallDone * 100 / overallTotal)
    val modeLabel = if (mode == AppMode.DSA) "DSA TRACK" else "AI SIMULATION TRACK"

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    OverallRing(pct = overallPct)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            modeLabel,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                        )
                        Text(
                            "$overallDone of $overallTotal done",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFB923C),
                                modifier = Modifier.size(15.dp),
                            )
                            Text(
                                "  ${streak.coerceAtLeast(1)} day streak · keep it going!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                "By category",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
            )
        }

        items(groups) { group ->
            val total = group.topics.size
            val done = group.topics.count { it.id in completedIds }
            val pct = if (total == 0) 0 else (done * 100 / total)
            CategoryProgressCard(group = group, done = done, total = total, pct = pct)
        }
    }
}

@Composable
private fun OverallRing(pct: Int) {
    val accent = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.surfaceVariant
    Box(modifier = Modifier.size(96.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 11.dp.toPx()
            val inset = stroke / 2
            val arcSize = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
            val topLeft = androidx.compose.ui.geometry.Offset(inset, inset)
            drawArc(
                color = track,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke),
            )
            drawArc(
                color = accent,
                startAngle = -90f,
                sweepAngle = pct * 3.6f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
        Text(
            "$pct%",
            fontFamily = SpaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
    }
}

@Composable
private fun CategoryProgressCard(group: ProgressGroup, done: Int, total: Int, pct: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 11.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        Brush.linearGradient(listOf(group.color, group.color.copy(alpha = 0.6f))),
                        RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    resolveIcon(group.iconName),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(group.title, fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                    Text("$pct%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = group.color)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(99.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((pct / 100f).coerceIn(0f, 1f))
                            .background(group.color, RoundedCornerShape(99.dp)),
                    )
                }
                Text(
                    "$done / $total topics",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}
