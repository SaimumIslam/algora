package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

// Authored content for The Perceptron — the ML/DL entry point the mock names but doesn't fully
// specify. Follows the same 7-section template; sim is the classifier-gate solver.
internal val perceptronContent = TopicContent(
    topicId = "perceptron",
    whatIsIt = listOf(
        "The perceptron is the simplest artificial neuron: it takes a few numbers, weighs each one, adds a bias, and fires a 0 or a 1.",
        "Introduced by Frank Rosenblatt in 1958, it is the single building block that, stacked and trained, becomes a modern neural network.",
    ),
    steps = listOf(
        StepCard(1, "Weighted Sum", "Each input xᵢ is multiplied by a weight wᵢ and summed together with a bias term b.", 0xFF6366F1),
        StepCard(2, "Activation", "Pass the sum through a step function: output 1 if it is ≥ 0, otherwise 0.", 0xFF60A5FA),
        StepCard(3, "The Decision Boundary", "The weights and bias define a straight line; points on one side fire, points on the other don't.", 0xFFF59E0B),
        StepCard(4, "Learning Rule", "For every wrong prediction, nudge each weight toward the correct answer: w := w + η(target − output)x.", 0xFF10B981),
    ),
    formulas = listOf(
        FormulaEntry("Weighted sum", "z = Σ wᵢxᵢ + b", "The pre-activation, a dot product plus bias."),
        FormulaEntry("Activation", "ŷ = 1 if z ≥ 0 else 0", "The Heaviside step decides the class."),
        FormulaEntry("Update rule", "wᵢ := wᵢ + η(y − ŷ)xᵢ", "η is the learning rate; only mistakes change weights."),
    ),
    notationKey = listOf(
        NotationEntry("xᵢ", "the i-th input feature"),
        NotationEntry("wᵢ", "the weight on input xᵢ"),
        NotationEntry("b", "the bias (shifts the boundary)"),
        NotationEntry("η", "learning rate"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "Perceptron — predict & train (Python)",
            accentColor = 0xFF6366F1,
            code = """
                import numpy as np

                def step(z):
                    return 1 if z >= 0 else 0

                def train(X, y, lr=0.1, epochs=20):
                    w = np.zeros(X.shape[1])
                    b = 0.0
                    for _ in range(epochs):
                        for xi, target in zip(X, y):
                            pred = step(w @ xi + b)
                            error = target - pred
                            w += lr * error * xi   # only moves on mistakes
                            b += lr * error
                    return w, b
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.PerceptronVisualizer,
    applications = listOf(
        ApplicationCard("chip", 0xFF6366F1, "Linear Classifiers", "Spam vs not-spam and other yes/no decisions with a single threshold."),
        ApplicationCard("network", 0xFFEC4899, "Neural Network Neurons", "Stacked perceptrons with smooth activations form deep networks."),
        ApplicationCard("trend", 0xFF10B981, "Online Learning", "Updates one example at a time, ideal for streaming data."),
    ),
    takeaways = listOf(
        "A perceptron is one neuron: weighted sum, bias, step activation.",
        "It can only separate classes that a single straight line divides.",
        "XOR is not linearly separable — the limitation that motivated multi-layer networks.",
        "Its learning rule updates weights only when it makes a mistake.",
    ),
    crossLinks = listOf(
        CrossLink("linear_regression", "Linear Regression"),
    ),
)
