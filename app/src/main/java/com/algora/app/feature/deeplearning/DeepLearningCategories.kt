package com.algora.app.feature.deeplearning

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Section

// Colors/icons pulled verbatim from docs/design/Algora.dc.html's cats()['dl'].
object DeepLearningCategories {
    val fundamentals = Category("dl_fundamentals", "Fundamentals", Section.DL, 0xFFEC4899, "network")
    val architectures = Category("dl_architectures", "Architectures", Section.DL, 0xFF8B5CF6, "chip")

    val all = listOf(fundamentals, architectures)
}
