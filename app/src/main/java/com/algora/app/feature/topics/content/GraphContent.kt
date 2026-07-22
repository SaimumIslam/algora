package com.algora.app.feature.topics.content

import com.algora.app.core.data.model.ApplicationCard
import com.algora.app.core.data.model.CodeBlock
import com.algora.app.core.data.model.CrossLink
import com.algora.app.core.data.model.FormulaEntry
import com.algora.app.core.data.model.NotationEntry
import com.algora.app.core.data.model.SimulationType
import com.algora.app.core.data.model.StepCard
import com.algora.app.core.data.model.TopicContent

internal val graphContent = TopicContent(
    topicId = "graph",
    whatIsIt = listOf(
        "A graph is a set of nodes (vertices) connected by edges, which may be directed or undirected and weighted or unweighted.",
        "Think of a city's road map: intersections are vertices, roads are edges, and one-way streets are directed edges.",
    ),
    steps = listOf(
        StepCard(1, "Vertices & Edges", "A graph is defined by a set of vertices V and a set of edges E connecting pairs of vertices.", 0xFF3B82F6),
        StepCard(2, "Adjacency Representation", "Graphs are stored as an adjacency list (per-vertex neighbor list) or adjacency matrix (V×V grid).", 0xFF8B5CF6),
        StepCard(3, "Traversal", "BFS explores level by level using a queue; DFS explores as deep as possible first using a stack or recursion.", 0xFFF59E0B),
        StepCard(4, "Weighted Paths", "When edges carry weights, shortest-path algorithms like Dijkstra's replace simple traversal.", 0xFFEC4899),
    ),
    formulas = listOf(
        FormulaEntry("BFS / DFS traversal", "O(V + E)", "Every vertex and edge is visited once."),
        FormulaEntry("Adjacency matrix space", "O(V²)", "One cell per possible vertex pair, regardless of edge count."),
        FormulaEntry("Adjacency list space", "O(V + E)", "One entry per vertex plus one per edge — better for sparse graphs."),
    ),
    notationKey = listOf(
        NotationEntry("V", "number of vertices"),
        NotationEntry("E", "number of edges"),
        NotationEntry("adj[u]", "the list of neighbors of vertex u"),
    ),
    codeBlocks = listOf(
        CodeBlock(
            title = "Graph — adjacency list + BFS",
            accentColor = 0xFF6366F1,
            code = """
                class Graph {
                    private val adjacency = mutableMapOf<Int, MutableList<Int>>()

                    fun addEdge(from: Int, to: Int) {
                        adjacency.getOrPut(from) { mutableListOf() }.add(to)
                        adjacency.getOrPut(to) { mutableListOf() }.add(from)
                    }

                    fun bfs(start: Int): List<Int> {
                        val visited = mutableSetOf(start)
                        val order = mutableListOf<Int>()
                        val queue = ArrayDeque(listOf(start))

                        while (queue.isNotEmpty()) {
                            val node = queue.removeFirst()
                            order.add(node)
                            for (neighbor in adjacency[node].orEmpty()) {
                                if (visited.add(neighbor)) queue.addLast(neighbor)
                            }
                        }
                        return order
                    }
                }
            """.trimIndent(),
        ),
    ),
    simulation = SimulationType.GraphVisualizer,
    applications = listOf(
        ApplicationCard("map", 0xFF3B82F6, "Navigation & Routing", "Road networks and flight routes are graphs; shortest-path algorithms power turn-by-turn directions."),
        ApplicationCard("share", 0xFF8B5CF6, "Social Networks", "People are vertices, friendships/follows are edges — traversal powers 'friends of friends' features."),
        ApplicationCard("network", 0xFF10B981, "Dependency Resolution", "Package managers model dependencies as a directed graph and topologically sort it to install in order."),
    ),
    takeaways = listOf(
        "A graph generalizes trees and linked lists — any node can connect to any other node, including cycles.",
        "Adjacency lists suit sparse graphs (few edges); adjacency matrices suit dense graphs (many edges).",
        "BFS finds shortest paths in unweighted graphs; DFS is naturally suited to cycle detection and topological sort.",
        "Weighted shortest-path problems need Dijkstra's, Bellman-Ford, or Floyd-Warshall instead of plain BFS.",
    ),
    crossLinks = listOf(
        // DSA ↔ AI bridge: graph traversal underpins tree-search planning in RL.
        CrossLink("mcts", "Monte Carlo Tree Search"),
    ),
)
