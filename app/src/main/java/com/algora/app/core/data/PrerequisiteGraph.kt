package com.algora.app.core.data

// Curated learning-order DAG over core DSA topics. Edge topic → [prereqs] means "learn the prereqs
// first". Not exhaustive (the taxonomy is large) — it covers the backbone so the detail page can
// show a "learn first / unlocks" path. Every id here must exist in TopicRegistry.
object PrerequisiteGraph {
    private val prerequisites: Map<String, List<String>> = mapOf(
        // Linear structures
        "string" to listOf("array"),
        "singly_linked_list" to listOf("array"),
        "doubly_linked_list" to listOf("singly_linked_list"),
        "stack" to listOf("array"),
        "queue" to listOf("array"),
        "hash_table" to listOf("array"),
        // Non-linear
        "tree" to listOf("singly_linked_list"),
        "binary_search_tree" to listOf("tree"),
        "heap" to listOf("tree", "array"),
        "trie" to listOf("tree"),
        "graph" to listOf("tree"),
        // Traversal / search
        "binary_search" to listOf("array"),
        "bfs" to listOf("graph", "queue"),
        "dfs" to listOf("graph", "stack"),
        // Sorting
        "merge_sort" to listOf("array", "factorial"),
        "quick_sort" to listOf("array"),
        "heap_sort" to listOf("heap"),
        // Graph algorithms
        "dijkstras_algorithm" to listOf("graph", "heap"),
        // Recursion / backtracking
        "fibonacci_recursive" to listOf("factorial"),
        "tower_of_hanoi" to listOf("fibonacci_recursive"),
        "n_queens" to listOf("dfs"),
        // Dynamic programming
        "fibonacci_dp" to listOf("fibonacci_recursive"),
        "longest_common_subsequence" to listOf("fibonacci_dp"),
        "edit_distance" to listOf("longest_common_subsequence"),
        "coin_change" to listOf("fibonacci_dp"),
        "knapsack_01" to listOf("coin_change"),
    )

    fun prereqsOf(topicId: String): List<String> = prerequisites[topicId].orEmpty()

    // Reverse edges: topics that list [topicId] among their prerequisites.
    fun unlockedBy(topicId: String): List<String> =
        prerequisites.filter { topicId in it.value }.keys.toList()
}
