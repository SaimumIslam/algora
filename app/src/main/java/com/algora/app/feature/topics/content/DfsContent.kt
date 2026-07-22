package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val dfsContent = TopicContent(
    topicId = "dfs",
    whatIsIt = listOf(
        "Depth-first search explores a graph by going as deep as possible along one path before backtracking to try the next unexplored branch.",
        "It uses a stack — explicit, or implicitly via recursion — so the most recently discovered node is the next one expanded.",
    ),
    steps = listOf(
        StepCard(1, "Visit & Mark", "Start at a source node, mark it visited so it's never processed twice.", 0xFF10B981),
        StepCard(2, "Go Deeper", "Recurse into the first unvisited neighbor, then its unvisited neighbor, and so on.", 0xFF3B82F6),
        StepCard(3, "Backtrack", "When a node has no unvisited neighbors, return to the previous node and try its next branch.", 0xFFF59E0B),
        StepCard(4, "Repeat Until Done", "Continue until every node reachable from the source has been visited.", 0xFFEC4899),
    ),
    formulas = listOf(
        FormulaEntry("Time", "O(V + E)", "Each vertex and edge is examined once."),
        FormulaEntry("Space", "O(V)", "Recursion/stack depth plus the visited set."),
        FormulaEntry("Traversal order", "Deepest-first", "Explores a branch fully before its siblings."),
    ),
    notationKey = listOf(
        NotationEntry("V", "number of vertices (nodes)"),
        NotationEntry("E", "number of edges"),
        NotationEntry("visited", "set marking already-explored nodes"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "DFS — recursive",
            accentColor = 0xFF6366F1,
            code = """
                fun dfs(
                    node: Int,
                    adjacency: Map<Int, List<Int>>,
                    visited: MutableSet<Int> = mutableSetOf(),
                ) {
                    if (!visited.add(node)) return   // already seen
                    for (next in adjacency[node].orEmpty()) {
                        dfs(next, adjacency, visited)
                    }
                }
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.GraphVisualizer,
    applications = listOf(
        ApplicationCard("share", 0xFF10B981, "Cycle & Connectivity Checks", "DFS detects cycles, finds connected components, and drives topological sort on dependency graphs."),
        ApplicationCard("map", 0xFF3B82F6, "Maze & Puzzle Solving", "Exploring one path fully before backing up is the backbone of backtracking search."),
        ApplicationCard("link", 0xFFF59E0B, "Path & Tree Traversal", "Pre/in/post-order tree walks and 'does a path exist?' queries are DFS in disguise."),
    ),
    takeaways = listOf(
        "DFS goes deep first and backtracks, driven by a stack (often the call stack via recursion).",
        "It runs in O(V + E) and needs a visited set to avoid revisiting nodes in cyclic graphs.",
        "It underpins cycle detection, topological sort, connected components, and backtracking.",
        "Use DFS when you must exhaust paths; use BFS when you need the shortest unweighted path.",
    ),
    crossLinks = listOf(
        CrossLink("bfs", "Breadth-First Search (BFS)"),
        CrossLink("graph", "Graph"),
    ),
)
