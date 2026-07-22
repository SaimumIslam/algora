package com.algora.app.feature.interviewprep

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Topic

private fun topic(id: String, name: String, category: Category, tagline: String, isPremium: Boolean = false) = Topic(
    id = id,
    name = name,
    categoryId = category.id,
    tagline = tagline,
    description = tagline,
    iconName = category.iconName,
    accentColor = category.accentColor,
    isPremium = isPremium,
)

private val patterns = InterviewPrepCategories.patterns
private val companySets = InterviewPrepCategories.companySets
private val mock = InterviewPrepCategories.mock

private val patternTopics = listOf(
    topic("sliding_window_pattern", "Sliding Window Pattern", patterns, "Recognize and apply the sliding-window pattern."),
    topic("two_pointer_pattern", "Two Pointer Pattern", patterns, "Recognize and apply the two-pointer pattern."),
    topic("fast_slow_pointers", "Fast & Slow Pointers", patterns, "Cycle detection and midpoint-finding pattern."),
    topic("merge_intervals_pattern", "Merge Intervals", patterns, "Recognize and apply the merge-intervals pattern."),
    topic("top_k_pattern", "Top-K Pattern", patterns, "Recognize and apply the top-k heap pattern."),
)

private val companySetTopics = listOf(
    topic("faang_set", "FAANG Set", companySets, "Curated question set from large tech companies.", isPremium = true),
    topic("startup_set", "Startup Set", companySets, "Curated question set from startup-style interviews.", isPremium = true),
    topic("finance_trading_set", "Finance / Trading Set", companySets, "Curated question set from finance and trading firms.", isPremium = true),
)

private val mockTopics = listOf(
    topic("timed_mock_interview", "Timed Mock Interview", mock, "Full-length interview under a countdown timer."),
    topic("behavioral_question_bank", "Behavioral Question Bank", mock, "Common behavioral questions with guidance.", isPremium = true),
    topic("system_design_primer", "System Design Primer", mock, "Foundations for system design interview rounds.", isPremium = true),
)

object InterviewPrepTopics {
    val topics: List<Topic> = patternTopics + companySetTopics + mockTopics

    fun find(topicId: String): Topic? = topics.find { it.id == topicId }
}
