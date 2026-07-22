package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val longestCommonSubsequenceContent = TopicContent(
    topicId = "longest_common_subsequence",
    whatIsIt = listOf(
        "The longest common subsequence (LCS) of two strings is the longest sequence of characters appearing in both in the same order, though not necessarily contiguously.",
        "It's a foundational two-string DP: a table indexed by prefixes of each string, where each cell extends or inherits the best subsequence found so far.",
    ),
    steps = listOf(
        StepCard(1, "Compare Prefixes", "dp[i][j] is the LCS length of the first i chars of A and first j chars of B.", 0xFF10B981),
        StepCard(2, "Match Extends", "If A[i] == B[j], the LCS grows by one: dp[i][j] = dp[i−1][j−1] + 1.", 0xFF3B82F6),
        StepCard(3, "Mismatch Inherits", "Otherwise take the better of dropping a character from A or from B.", 0xFFF59E0B),
        StepCard(4, "Fill & Trace", "Fill the grid row by row; walk back from the corner to reconstruct the actual subsequence.", 0xFFEC4899),
    ),
    formulas = listOf(
        FormulaEntry("Match", "dp[i][j] = dp[i−1][j−1] + 1", "When characters agree."),
        FormulaEntry("Mismatch", "dp[i][j] = max(dp[i−1][j], dp[i][j−1])", "Drop one character from either string."),
        FormulaEntry("Time / Space", "O(m · n)", "One cell per prefix pair; space reducible to O(min(m, n))."),
    ),
    notationKey = listOf(
        NotationEntry("m, n", "lengths of the two strings"),
        NotationEntry("subsequence", "in-order but not necessarily contiguous characters"),
        NotationEntry("dp[i][j]", "LCS length of the two prefixes"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "LCS length — tabulation",
            accentColor = 0xFF6366F1,
            code = """
                fun lcs(a: String, b: String): Int {
                    val dp = Array(a.length + 1) { IntArray(b.length + 1) }
                    for (i in 1..a.length) for (j in 1..b.length) {
                        dp[i][j] = if (a[i - 1] == b[j - 1]) {
                            dp[i - 1][j - 1] + 1
                        } else {
                            maxOf(dp[i - 1][j], dp[i][j - 1])
                        }
                    }
                    return dp[a.length][b.length]
                }
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.DpGridVisualizer,
    applications = listOf(
        ApplicationCard("link", 0xFF10B981, "Diff Tools", "Line-based LCS is what git diff and file-compare tools use to align versions."),
        ApplicationCard("flask", 0xFF3B82F6, "Bioinformatics", "Aligning DNA/protein sequences to measure similarity builds on LCS-style DP."),
        ApplicationCard("search", 0xFFF59E0B, "Plagiarism & Similarity", "Comparing documents for shared ordered content uses LCS as a similarity signal."),
    ),
    takeaways = listOf(
        "LCS is the archetypal two-string DP over prefix pairs.",
        "Matching characters extend the diagonal; mismatches inherit the best neighbor.",
        "It runs in O(m · n), reducible to O(min(m, n)) space if only the length is needed.",
        "Diffing, sequence alignment, and edit distance are all close relatives.",
    ),
    crossLinks = listOf(
        CrossLink("edit_distance", "Edit Distance"),
        CrossLink("longest_increasing_subsequence", "Longest Increasing Subsequence"),
    ),
)
