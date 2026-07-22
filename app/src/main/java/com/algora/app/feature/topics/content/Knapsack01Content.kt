package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val knapsack01Content = TopicContent(
    topicId = "knapsack_01",
    whatIsIt = listOf(
        "The 0/1 knapsack problem maximizes the value packed into a weight-limited bag where each item is taken whole or left behind — no fractions allowed.",
        "That all-or-nothing constraint defeats greedy strategies, so it's solved by dynamic programming over a table of (items considered, capacity used).",
    ),
    steps = listOf(
        StepCard(1, "Define the State", "dp[i][w] = the best value using the first i items within capacity w.", 0xFF10B981),
        StepCard(2, "Take or Leave", "For item i, choose the better of skipping it or including it (value plus dp for the reduced capacity).", 0xFF3B82F6),
        StepCard(3, "Fill the Table", "Build up from zero items and zero capacity to the full problem.", 0xFFF59E0B),
        StepCard(4, "Read the Answer", "dp[n][W] holds the maximum achievable value; backtrack the table to recover which items.", 0xFFEC4899),
    ),
    formulas = listOf(
        FormulaEntry("Transition", "dp[i][w] = max(dp[i−1][w], vᵢ + dp[i−1][w−wᵢ])", "Leave vs take item i."),
        FormulaEntry("Time", "O(n · W)", "One cell per item-capacity pair — pseudo-polynomial."),
        FormulaEntry("Space", "O(n · W) → O(W)", "A single rolling row suffices if items aren't recovered."),
    ),
    notationKey = listOf(
        NotationEntry("W", "the bag's weight capacity"),
        NotationEntry("vᵢ, wᵢ", "value and weight of item i"),
        NotationEntry("dp[i][w]", "best value from first i items in capacity w"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "0/1 knapsack — 1D DP",
            accentColor = 0xFF6366F1,
            code = """
                fun knapsack(values: IntArray, weights: IntArray, capacity: Int): Int {
                    val dp = IntArray(capacity + 1)
                    for (i in values.indices) {
                        // Iterate capacity downward so each item is used at most once.
                        for (w in capacity downTo weights[i]) {
                            dp[w] = maxOf(dp[w], values[i] + dp[w - weights[i]])
                        }
                    }
                    return dp[capacity]
                }
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.NotYetAvailable,
    applications = listOf(
        ApplicationCard("finance", 0xFF10B981, "Budget Selection", "Pick the most valuable indivisible investments or purchases under a fixed budget."),
        ApplicationCard("chip", 0xFF3B82F6, "Resource Packing", "Fit whole jobs, files, or containers into fixed capacity to maximize value."),
        ApplicationCard("target", 0xFFF59E0B, "Feature/Project Selection", "Choose which whole projects to fund within a capacity constraint."),
    ),
    takeaways = listOf(
        "0/1 knapsack is DP over (items, capacity) with a take-or-leave choice per item.",
        "It runs in pseudo-polynomial O(n · W); iterating capacity downward keeps items single-use.",
        "Greedy fails here — that's exactly what separates it from fractional knapsack.",
        "Backtracking the filled table recovers which items were chosen, not just the value.",
    ),
    crossLinks = listOf(
        CrossLink("fractional_knapsack", "Fractional Knapsack"),
        CrossLink("subset_sum", "Subset Sum"),
    ),
)
