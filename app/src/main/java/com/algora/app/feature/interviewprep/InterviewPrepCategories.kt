package com.algora.app.feature.interviewprep

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Section

// Colors/icons pulled verbatim from docs/design/Algora.dc.html's cats() for Interview Prep.
// Full mock question content is Phase 6's job — this phase only builds the browser chrome + row metadata.
object InterviewPrepCategories {
    val patterns = Category("interview_patterns", "Patterns", Section.INTERVIEW_PREP, 0xFFF59E0B, "help")
    val companySets = Category("interview_company_sets", "Company Sets", Section.INTERVIEW_PREP, 0xFF3B82F6, "browser")
    val mock = Category("interview_mock", "Mock", Section.INTERVIEW_PREP, 0xFF8B5CF6, "history")

    val all = listOf(patterns, companySets, mock)
}
