package com.algora.app.feature.review

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.algora.app.core.data.TopicRegistry
import com.algora.app.core.data.settings.SettingsRepository
import com.algora.app.core.data.settings.settingsDataStore
import com.algora.app.core.ui.theme.SimColors
import com.algora.app.core.ui.theme.SpaceGrotesk
import com.algora.app.feature.topics.content.TopicContentProvider
import kotlinx.coroutines.launch

private data class ReviewCard(val key: String, val topicName: String, val takeaway: String)

// Every takeaway across every authored topic, keyed stably as "topicId#index".
private fun allReviewCards(): List<ReviewCard> =
    TopicContentProvider.all.flatMap { (topicId, content) ->
        val name = TopicRegistry.find(topicId)?.name ?: topicId
        content.takeaways.mapIndexed { i, t -> ReviewCard("$topicId#$i", name, t) }
    }

@Composable
fun ReviewScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val settings = remember { SettingsRepository(context.settingsDataStore) }
    val scope = rememberCoroutineScope()
    val allCards = remember { allReviewCards() }
    val today = System.currentTimeMillis() / 86_400_000L

    // null = still loading the SRS state from DataStore.
    val srsMap by settings.srs.collectAsState(initial = null)

    // Freeze the due queue once, when state first loads, so grading doesn't reshuffle mid-session.
    var queue by remember { mutableStateOf<List<ReviewCard>?>(null) }
    if (queue == null && srsMap != null) {
        val map = srsMap!!
        queue = allCards.filter { c -> map[c.key]?.let { it.dueDay <= today } ?: true }.shuffled()
    }

    var index by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var reviewed by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(onBack)

        val cards = queue
        when {
            cards == null -> CenterMessage("Loading review…")
            cards.isEmpty() -> CenterMessage("All caught up — nothing due for review right now. 🎉")
            index >= cards.size -> CenterMessage("Session complete — $reviewed card${if (reviewed == 1) "" else "s"} reviewed. 🎉")
            else -> {
                val card = cards[index]
                Text(
                    "Due today · ${index + 1} of ${cards.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { flipped = !flipped },
                    shape = RoundedCornerShape(20.dp),
                    color = if (flipped) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            card.topicName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.size(18.dp))
                        if (flipped) {
                            Text(card.takeaway, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                        } else {
                            Text(
                                "Recall a key takeaway",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text("Tap to reveal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                if (flipped) {
                    fun grade(quality: Int) {
                        scope.launch { settings.reviewCard(card.key, quality) }
                        reviewed++
                        index++
                        flipped = false
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        GradeButton("Again", SimColors.Red, Modifier.weight(1f)) { grade(2) }
                        GradeButton("Good", SimColors.Blue, Modifier.weight(1f)) { grade(4) }
                        GradeButton("Easy", SimColors.Green, Modifier.weight(1f)) { grade(5) }
                    }
                } else {
                    Text(
                        "Tap the card to reveal, then grade your recall.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun GradeButton(label: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        modifier = modifier,
    ) { Text(label) }
}

@Composable
private fun CenterMessage(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp),
        )
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Spaced Repetition",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }
        HorizontalDivider()
    }
}
