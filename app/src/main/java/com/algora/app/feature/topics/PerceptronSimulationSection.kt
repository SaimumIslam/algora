package com.algora.app.feature.topics

import androidx.compose.runtime.Composable

// The Perceptron topic's lab is the AND/OR/XOR gate solver — now a thin wrapper over the reusable
// ClassifierPlaygroundSection (gate config). Screen is unchanged from the original implementation.
@Composable
fun PerceptronSimulationSection() {
    ClassifierPlaygroundSection(gateClassifierConfig)
}
