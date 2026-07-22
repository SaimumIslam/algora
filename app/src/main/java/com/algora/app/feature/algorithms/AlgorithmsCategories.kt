package com.algora.app.feature.algorithms

import com.algora.app.core.data.model.Category
import com.algora.app.core.data.model.Section

// Colors/icons pulled verbatim from docs/design/Algora.dc.html's cats() for Algorithms (9 numbered groups).
object AlgorithmsCategories {
    val sorting = Category("algo_sorting", "1 · Sorting", Section.ALGORITHMS, 0xFF3B82F6, "stack")
    val searching = Category("algo_searching", "2 · Searching", Section.ALGORITHMS, 0xFF06B6D4, "search")
    val recursion = Category("algo_recursion", "3 · Recursion & Backtracking", Section.ALGORITHMS, 0xFF8B5CF6, "share")
    val divideConquer = Category("algo_divide_conquer", "4 · Divide and Conquer", Section.ALGORITHMS, 0xFFF59E0B, "chip")
    val greedy = Category("algo_greedy", "5 · Greedy Algorithms", Section.ALGORITHMS, 0xFF10B981, "trend")
    val dynamicProgramming = Category("algo_dp", "6 · Dynamic Programming", Section.ALGORITHMS, 0xFFEC4899, "stack")
    val graphAlgorithms = Category("algo_graph", "7 · Graph Algorithms", Section.ALGORITHMS, 0xFF3B82F6, "share")
    val pathfinding = Category("algo_pathfinding", "8 · Pathfinding", Section.ALGORITHMS, 0xFF06B6D4, "map")
    val miscAdvanced = Category("algo_misc", "9 · Misc & Advanced", Section.ALGORITHMS, 0xFF8B5CF6, "chip")

    val all = listOf(sorting, searching, recursion, divideConquer, greedy, dynamicProgramming, graphAlgorithms, pathfinding, miscAdvanced)
}
