package com.algora.app.feature.interviewprep.quiz

// Maps a topic id to its timed quiz. Same role as AnalysisToolRegistry: topics listed here bypass
// the 7-section TopicContent template and render QuizScreen instead. Others fall through normally.
object QuizRegistry {
    private val quizzes: Map<String, Quiz> = mapOf(
        "timed_mock_interview" to timedMockInterview,
        "faang_set" to faangSet,
    )

    fun get(topicId: String): Quiz? = quizzes[topicId]
}
