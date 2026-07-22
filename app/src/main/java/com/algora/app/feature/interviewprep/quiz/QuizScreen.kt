package com.algora.app.feature.interviewprep.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algora.app.core.data.model.Difficulty
import com.algora.app.core.ui.theme.SimColors
import com.algora.app.core.ui.theme.SpaceGrotesk
import kotlinx.coroutines.delay

private val CorrectGreen = Color(0xFF16A34A)
private val WrongRed = Color(0xFFEF4444)

@Composable
fun QuizScreen(
    quiz: Quiz,
    onBack: () -> Unit,
    onTopicClick: (String) -> Unit,
    onFinish: () -> Unit,
) {
    var attempt by remember { mutableStateOf(0) }
    // A fresh attempt rebuilds all per-run state (answers, index, timer, phase).
    key(attempt) {
        QuizRunner(
            quiz = quiz,
            onBack = onBack,
            onTopicClick = onTopicClick,
            onFinish = onFinish,
            onRetry = { attempt++ },
        )
    }
}

@Composable
private fun QuizRunner(
    quiz: Quiz,
    onBack: () -> Unit,
    onTopicClick: (String) -> Unit,
    onFinish: () -> Unit,
    onRetry: () -> Unit,
) {
    val answers: SnapshotStateList<Int?> = remember { List<Int?>(quiz.questions.size) { null }.toMutableStateList() }
    var index by remember { mutableIntStateOf(0) }
    var remaining by remember { mutableIntStateOf(quiz.timeLimitSeconds) }
    var finished by remember { mutableStateOf(false) }

    // Countdown; expiry auto-submits.
    LaunchedEffect(finished) {
        while (!finished && remaining > 0) {
            delay(1000)
            remaining--
        }
        if (remaining <= 0) finished = true
    }

    if (finished) {
        LaunchedEffect(Unit) { onFinish() }
        QuizResults(
            quiz = quiz,
            answers = answers,
            timeUsed = quiz.timeLimitSeconds - remaining,
            onBack = onBack,
            onRetry = onRetry,
            onTopicClick = onTopicClick,
        )
        return
    }

    val question = quiz.questions[index]

    Column(modifier = Modifier.fillMaxSize()) {
        QuizHeader(title = quiz.title, remaining = remaining, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                "Question ${index + 1} of ${quiz.questions.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
            )
            LinearProgressIndicator(
                progress = { (index + 1) / quiz.questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(modifier = Modifier.padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagChip(question.patternTag, MaterialTheme.colorScheme.primary)
                question.companyTag?.let { TagChip(it, Color(0xFF3B82F6)) }
                DifficultyChip(question.difficulty)
            }

            Text(
                question.prompt,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 14.dp, bottom = 4.dp),
            )

            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                question.options.forEachIndexed { i, option ->
                    OptionCard(
                        text = option,
                        selected = answers[index] == i,
                        onClick = { answers[index] = i },
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (index > 0) {
                OutlinedButton(onClick = { index-- }, modifier = Modifier.weight(1f)) { Text("Previous") }
            }
            Button(
                onClick = { if (index < quiz.questions.lastIndex) index++ else finished = true },
                modifier = Modifier.weight(1f),
            ) {
                Text(if (index < quiz.questions.lastIndex) "Next" else "Finish")
            }
        }
    }
}

@Composable
private fun QuizHeader(title: String, remaining: Int, onBack: () -> Unit) {
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
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            TimerPill(remaining)
            Spacer(modifier = Modifier.size(6.dp))
        }
        HorizontalDivider()
    }
}

@Composable
private fun TimerPill(remaining: Int) {
    val urgent = remaining <= 30
    val color = if (urgent) WrongRed else MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Timer, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            formatTime(remaining),
            color = color,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGrotesk,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun OptionCard(text: String, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(if (selected) 2.dp else 1.dp, border),
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 14.dp),
        )
    }
}

@Composable
private fun TagChip(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun DifficultyChip(difficulty: Difficulty) {
    val (label, color) = when (difficulty) {
        Difficulty.BEGINNER -> "Easy" to Color(0xFF16A34A)
        Difficulty.INTERMEDIATE -> "Medium" to Color(0xFFF59E0B)
        Difficulty.ADVANCED -> "Hard" to Color(0xFFEF4444)
    }
    TagChip(label, color)
}

@Composable
private fun QuizResults(
    quiz: Quiz,
    answers: List<Int?>,
    timeUsed: Int,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onTopicClick: (String) -> Unit,
) {
    val correct = quiz.questions.indices.count { answers[it] == quiz.questions[it].correctIndex }
    val total = quiz.questions.size
    val pct = if (total == 0) 0 else correct * 100 / total

    Column(modifier = Modifier.fillMaxSize()) {
        QuizHeaderStatic(title = "Results", onBack = onBack)

        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = if (pct >= 60) CorrectGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, if (pct >= 60) CorrectGreen.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline),
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$correct / $total",
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            color = if (pct >= 60) CorrectGreen else MaterialTheme.colorScheme.onSurface,
                        )
                        Text("$pct% correct · finished in ${formatTime(timeUsed)}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item {
                Text(
                    "Review",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 22.dp, bottom = 8.dp),
                )
            }

            items(quiz.questions.size) { i ->
                ReviewCard(
                    question = quiz.questions[i],
                    chosen = answers[i],
                    onTopicClick = onTopicClick,
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back to Prep") }
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = SimColors.Green),
                        modifier = Modifier.weight(1f),
                    ) { Text("Retry") }
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(question: QuizQuestion, chosen: Int?, onTopicClick: (String) -> Unit) {
    val isCorrect = chosen == question.correctIndex
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(question.prompt, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Your answer: ${chosen?.let { question.options[it] } ?: "— (skipped)"}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCorrect) CorrectGreen else WrongRed,
                fontWeight = FontWeight.SemiBold,
            )
            if (!isCorrect) {
                Text(
                    "Correct: ${question.options[question.correctIndex]}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CorrectGreen,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text(
                question.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
            if (question.linkedTopicId != null) {
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .clickable { onTopicClick(question.linkedTopicId) }
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Study: ${question.linkedTopicLabel ?: "Related topic"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun QuizHeaderStatic(title: String, onBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }
        HorizontalDivider()
    }
}

private fun formatTime(seconds: Int): String {
    val s = seconds.coerceAtLeast(0)
    return "%d:%02d".format(s / 60, s % 60)
}
