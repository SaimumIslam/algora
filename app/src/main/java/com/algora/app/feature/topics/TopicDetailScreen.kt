package com.algora.app.feature.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algora.app.core.data.TopicRegistry
import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.Topic
import com.algora.app.core.data.model.TopicContent
import com.algora.app.core.data.progress.ProgressRepository
import com.algora.app.core.data.progress.progressDataStore
import com.algora.app.core.data.settings.SettingsRepository
import com.algora.app.core.data.settings.settingsDataStore
import com.algora.app.core.ui.components.DifficultyBadge
import com.algora.app.core.ui.components.resolveIcon
import com.algora.app.core.ui.theme.AlgoraCodeStyle
import com.algora.app.core.ui.theme.SpaceGrotesk
import com.algora.app.feature.analysis.tools.AnalysisToolRegistry
import com.algora.app.feature.interviewprep.quiz.QuizRegistry
import com.algora.app.feature.interviewprep.quiz.QuizScreen
import com.algora.app.feature.topics.content.TopicContentProvider
import kotlinx.coroutines.launch

@Composable
fun TopicDetailScreen(topicId: String, onBack: () -> Unit, onTopicClick: (String) -> Unit = {}) {
    val topic = remember(topicId) { TopicRegistry.find(topicId) }
    val content = remember(topicId) { TopicContentProvider.get(topicId) }

    val context = LocalContext.current
    val repository = remember { ProgressRepository(context.progressDataStore) }
    val completedIds by repository.completedTopicIds.collectAsState(initial = emptySet())
    val scope = rememberCoroutineScope()

    if (topic == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Content not available yet.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val isCompleted = topicId in completedIds

    val settings = remember { SettingsRepository(context.settingsDataStore) }
    LaunchedEffect(topicId) { settings.setLastOpened(topicId) }
    val bookmarks by settings.bookmarks.collectAsState(initial = emptySet())
    val isBookmarked = topicId in bookmarks
    val onToggleBookmark: () -> Unit = { scope.launch { settings.toggleBookmark(topicId) } }

    val quiz = remember(topicId) { QuizRegistry.get(topicId) }
    if (quiz != null) {
        QuizScreen(
            quiz = quiz,
            onBack = onBack,
            onTopicClick = onTopicClick,
            onFinish = { scope.launch { repository.markCompleted(topicId) } },
        )
        return
    }

    val toolContent = AnalysisToolRegistry.get(topicId)
    if (toolContent != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailHeader(title = topic.name, onBack = onBack, isBookmarked = isBookmarked, onToggleBookmark = onToggleBookmark)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                toolContent()
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { checked ->
                            scope.launch {
                                if (checked) repository.markCompleted(topicId) else repository.markIncomplete(topicId)
                            }
                        },
                    )
                    Text("Mark as complete", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        return
    }

    if (content == null) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailHeader(title = topic.name, onBack = onBack, isBookmarked = isBookmarked, onToggleBookmark = onToggleBookmark)
            ComingSoonBody(topic = topic)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DetailHeader(title = topic.name, onBack = onBack, isBookmarked = isBookmarked, onToggleBookmark = onToggleBookmark)

        LazyColumn(modifier = Modifier.weight(1f)) {
            item { HeroSection(topic, content) }
            item { SectionTitle("How Does It Work?") }
            item { HowItWorksSection(content.steps) }
            item { SectionTitle("The Mathematics") }
            item { MathSection(content.formulas, content.notationKey) }
            item { SectionTitle("Technical Deep Dive") }
            items(content.codeBlocks.withIndex().toList()) { (index, block) ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    CodeBlockCard(block = block, initiallyExpanded = index == 0)
                }
            }
            item { SectionTitle("Interactive Simulation") }
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    when (content.simulation) {
                        SimulationType.ArrayVisualizer -> ArraySimulationSection()
                        SimulationType.LinkedListVisualizer -> LinkedListSimulationSection()
                        SimulationType.StackVisualizer -> StackSimulationSection()
                        SimulationType.QueueVisualizer -> QueueSimulationSection()
                        SimulationType.GraphVisualizer -> GraphSimulationSection()
                        SimulationType.RegressionExplorer -> RegressionSimulationSection()
                        SimulationType.PerceptronVisualizer -> PerceptronSimulationSection()
                        SimulationType.ClassifierPlayground -> ClassifierPlaygroundSection(classifierConfigFor(topicId))
                        SimulationType.NotYetAvailable -> SimulationComingSoonCard()
                    }
                }
            }
            item { SectionTitle("Real-World Applications") }
            item { ApplicationsSection(content.applications) }
            item { TakeawaysSection(content.takeaways) }
            if (content.crossLinks.isNotEmpty()) {
                item { RelatedTopicsSection(content.crossLinks, onTopicClick) }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { checked ->
                            scope.launch {
                                if (checked) repository.markCompleted(topicId) else repository.markIncomplete(topicId)
                            }
                        },
                    )
                    Text("Mark as complete", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(
    title: String,
    onBack: () -> Unit,
    isBookmarked: Boolean? = null,
    onToggleBookmark: () -> Unit = {},
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            if (isBookmarked != null) {
                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 12.dp),
    )
}

// Shown for the ~70 topics that only have row metadata so far — full 7-section content
// authoring is a follow-up session (see docs/plan/phase-2-topic-browser-content.md).
@Composable
private fun ComingSoonBody(topic: Topic) {
    val accent = Color(topic.accentColor)
    val isDark = isSystemInDarkTheme()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = accent.copy(alpha = if (isDark) 0.14f else 0.10f),
            border = BorderStroke(1.dp, accent.copy(alpha = 0.22f)),
        ) {
            Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp), ambientColor = accent, spotColor = accent)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(resolveIcon(topic.iconName), contentDescription = null, tint = accent)
                }
                Spacer(modifier = Modifier.size(14.dp))
                Column {
                    Text(topic.name, style = MaterialTheme.typography.titleLarge)
                    Text(topic.tagline, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.size(22.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Full content coming soon",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    "This topic is in the taxonomy but its deep-dive content hasn't been authored yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SimulationComingSoonCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Simulation coming soon", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                "An interactive visualizer for this topic hasn't been built yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// Cross-links to related topics, possibly in the other app mode (Phase 5 DSA ↔ AI bridges).
@Composable
private fun RelatedTopicsSection(links: List<CrossLink>, onTopicClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Related Topics", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            links.forEach { link ->
                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { onTopicClick(link.topicId) },
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(link.label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroSection(topic: Topic, content: TopicContent) {
    val accent = Color(topic.accentColor)
    val isDark = isSystemInDarkTheme()

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = accent.copy(alpha = if (isDark) 0.14f else 0.10f),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.22f)),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp), ambientColor = accent, spotColor = accent)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(resolveIcon(topic.iconName), contentDescription = null, tint = accent)
                }
                Spacer(modifier = Modifier.size(14.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(topic.name, style = MaterialTheme.typography.titleLarge)
                        topic.difficulty?.let {
                            Spacer(modifier = Modifier.size(8.dp))
                            DifficultyBadge(it)
                        }
                    }
                    Text(topic.tagline, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.size(14.dp))
            Text("What is ${topic.name}?", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(8.dp))
            content.whatIsIt.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun HowItWorksSection(steps: List<StepCard>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        steps.forEach { step ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Row(modifier = Modifier.padding(15.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(step.accentColor).copy(alpha = 0.16f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            step.number.toString(),
                            color = Color(step.accentColor),
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    }
                    Spacer(modifier = Modifier.size(13.dp))
                    Column {
                        Text(step.title, style = MaterialTheme.typography.titleMedium)
                        Text(step.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun MathSection(formulas: List<FormulaEntry>, notationKey: List<NotationEntry>) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            formulas.forEachIndexed { index, formula ->
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(formula.label, style = MaterialTheme.typography.bodyLarge)
                        Text(formula.formula, style = AlgoraCodeStyle, color = MaterialTheme.colorScheme.primary)
                    }
                    Text(
                        formula.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                if (index < formulas.lastIndex) HorizontalDivider()
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text("Notation Key", style = MaterialTheme.typography.titleMedium)
            notationKey.forEach { entry ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(entry.symbol, style = AlgoraCodeStyle, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(entry.meaning, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun ApplicationsSection(applications: List<ApplicationCard>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        applications.forEach { app ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(app.accentColor).copy(alpha = 0.14f), RoundedCornerShape(13.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(resolveIcon(app.iconName), contentDescription = null, tint = Color(app.accentColor))
                    }
                    Spacer(modifier = Modifier.size(13.dp))
                    Column {
                        Text(app.title, style = MaterialTheme.typography.titleMedium)
                        Text(app.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private val TakeawayGreen = Color(0xFF16A34A)
private val TakeawayGreenBgLight = Color(0xFFE9F9EE)

@Composable
private fun TakeawaysSection(takeaways: List<String>) {
    val isDark = isSystemInDarkTheme()
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 22.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (isDark) TakeawayGreen.copy(alpha = 0.12f) else TakeawayGreenBgLight,
        border = BorderStroke(1.dp, TakeawayGreen.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = TakeawayGreen)
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    "Key Takeaways",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.size(14.dp))
            takeaways.forEach { takeaway ->
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = TakeawayGreen,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(takeaway, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
