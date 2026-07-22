package com.algora.app.feature.interviewprep.systemdesign

// Maps a topic id to its system-design primer, mirroring QuizRegistry / BehavioralRegistry.
object SystemDesignRegistry {
    private val primers: Map<String, SystemDesignPrimer> = mapOf(
        "system_design_primer" to systemDesignPrimer,
    )

    fun get(topicId: String): SystemDesignPrimer? = primers[topicId]
}
