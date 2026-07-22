package com.algora.app.feature.machinelearning

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Section

// Colors/icons pulled verbatim from docs/design/Algora.dc.html's cats()['ml'].
object MachineLearningCategories {
    val supervised = Category("ml_supervised", "Supervised", Section.ML, 0xFF6366F1, "robot")
    val unsupervised = Category("ml_unsupervised", "Unsupervised", Section.ML, 0xFF3B82F6, "share")

    val all = listOf(supervised, unsupervised)
}
