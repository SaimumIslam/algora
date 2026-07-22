package com.algora.app.feature.flashcards

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.algora.app.core.data.TopicRegistry
import com.algora.app.core.ui.theme.SpaceGrotesk
import com.algora.app.feature.topics.content.TopicContentProvider

private data class Flashcard(val topicName: String, val takeaway: String)

// Auto-generates review cards from every authored topic's Key Takeaways (Phase 7). No new content —
// the takeaways already written for each detail page become the flashcard backs.
@Composable
fun FlashcardScreen(onBack: () -> Unit) {
    val cards = remember {
        TopicContentProvider.all.flatMap { (topicId, content) ->
            val name = TopicRegistry.find(topicId)?.name ?: topicId
            content.takeaways.map { Flashcard(name, it) }
        }.shuffled()
    }

    if (cards.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(onBack)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No flashcards yet.", style = MaterialTheme.typography.bodyLarge)
            }
        }
        return
    }

    var index by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    val card = cards[index]

    Column(modifier = Modifier.fillMaxSize()) {
        Header(onBack)

        Text(
            "Card ${index + 1} of ${cards.size}",
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
                    Text(
                        "Tap to reveal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = { if (index > 0) { index--; flipped = false } },
                enabled = index > 0,
                modifier = Modifier.weight(1f),
            ) { Text("Previous") }
            Button(
                onClick = { if (index < cards.lastIndex) { index++; flipped = false } },
                enabled = index < cards.lastIndex,
                modifier = Modifier.weight(1f),
            ) { Text("Next") }
        }
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
                "Flashcard Review",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }
        HorizontalDivider()
    }
}
