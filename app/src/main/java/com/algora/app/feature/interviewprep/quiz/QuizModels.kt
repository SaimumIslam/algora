package com.algora.app.feature.interviewprep.quiz

import com.algora.app.core.data.model.Difficulty

// A single multiple-choice practice question, tagged by pattern and (optionally) company, with an
// optional cross-link back to the Algorithms topic it drills.
data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val patternTag: String,
    val difficulty: Difficulty,
    val explanation: String,
    val companyTag: String? = null,
    val linkedTopicId: String? = null,
    val linkedTopicLabel: String? = null,
)

// A timed set of questions. timeLimitSeconds drives the countdown; expiry auto-submits.
data class Quiz(
    val id: String,
    val title: String,
    val description: String,
    val timeLimitSeconds: Int,
    val questions: List<QuizQuestion>,
)
