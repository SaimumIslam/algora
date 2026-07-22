package com.algora.app.feature.nlp

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Section

// Colors/icons pulled verbatim from docs/design/Algora.dc.html's cats()['nlp'].
object NlpCategories {
    val preprocessing = Category("nlp_preprocessing", "Preprocessing", Section.NLP, 0xFF14B8A6, "globe")
    val modeling = Category("nlp_modeling", "Modeling", Section.NLP, 0xFF3B82F6, "network")

    val all = listOf(preprocessing, modeling)
}
