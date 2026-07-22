# Phase 2 — Topic Browser & Content Scale-out

Status: Browser shell done — bulk content authoring continues in a follow-up session
Depends on: Phase 1

## Goal
Build the category list screens and fill in remaining Data Structures + Algorithms topics using Phase 1's template.

## Rough scope
- Category list screens for all sections: grouped, icon + title, colored icon badge
- Row items: progress marker, title, chevron (free) or lock (premium — visual only, no enforcement yet)
- Per-category search bar
- Per-category progress bar
- Numbered sections for large taxonomies
- Remaining Data Structures + Algorithms topics: text/math/code sections filled in reusing Phase 1 composables
- Topics needing a simulation type not yet built (Phase 3) get a placeholder

## What actually got built (this session)

Full taxonomy (~27 DS topics, ~56 Algorithms topics, ~17 Analysis topics, ~11 Interview Prep
topics — from `docs/features.md` + the mock's `cats()`) now exists as row metadata across all
four DSA-mode tabs, each rendered through shared `core/ui/components/` composables
(`CategoryBrowserScreen`, `TopicRow`, `SectionHeader`, `CategorySearchField`,
`CategoryProgressBar`, `IconResolver`) matching `docs/design/Algora.dc.html`'s `isBrowser` block
exactly (colors, radii, gradients pulled from its `renderVals()`/`cats()`, not improvised).

Full 7-section detail content (matching Phase 1's Array template) was authored for a
**representative subset only** — not the whole taxonomy, which is too large for one pass:
- Data Structures: Array, Singly Linked List, Stack, Queue, Binary Search Tree, Graph
- Algorithms: Binary Search, Merge Sort, Bubble Sort, Breadth-First Search (BFS)

Every other topic (~70 of them) resolves via `TopicDetailScreen` to a "Full content coming
soon" placeholder (hero card + message) instead of crashing or showing a blank page.
`SimulationType.NotYetAvailable` topics (everything except Array, which still has the only real
simulation — the Canvas array visualizer) get a "Simulation coming soon" placeholder card rather
than silently omitting the section.

`isPremium` gating is a scoping decision, not spec-derived: within each category, the
"fundamentals" tier is free and everything past it is premium (see `DataStructuresTopics.kt` /
`AlgorithmsTopics.kt` / `AnalysisTopics.kt` / `InterviewPrepTopics.kt` for the exact split per
category). Locked rows still navigate normally — no enforcement, per the original scope.

Also fixed as part of this pass (small, but blocked accurate testing otherwise): bottom nav bar
now hides on the topic detail screen, matching the mock's fullscreen detail view (was a known
Phase 1 follow-up).

## Deferred to a later session
Authoring full 7-section content (steps/math/code/applications/takeaways) for the remaining
Data Structures + Algorithms topics. The row metadata, section grouping, and navigation are
already in place — each follow-up session just needs to add a `content/<Topic>Content.kt` file
and register it in `TopicContentProvider`, following the pattern in e.g.
`feature/topics/content/StackContent.kt`.

Progress: the free-tier core/non-linear Data Structures are now fully authored — Array, String,
Singly/Doubly Linked List, Stack, Queue, Hash Table, Tree, Binary Search Tree, Heap, Trie, Graph.
The free-tier Algorithms sorting + searching families are also complete — Bubble, Selection,
Insertion, Merge, Quick, Heap, Counting, Radix sort; Linear, Binary, Jump, Interpolation,
Exponential search. The free-tier greedy + graph families are done too — Fractional Knapsack,
Huffman Coding, Kruskal's MST, Prim's MST, Dijkstra's, Job Sequencing; BFS and DFS (DFS reuses the
existing GraphVisualizer). The free-tier recursion + divide-and-conquer families finished the set —
Factorial, Fibonacci (recursive), N-Queens, Sudoku Solver, Subset Sum, Permutation Generation;
Closest Pair of Points, Median of Medians, Tower of Hanoi.

**All free-tier Data Structures + Algorithms topics are now fully authored.** The premium DP family
is also done — Fibonacci (DP), Longest Common Subsequence, 0/1 Knapsack, Edit Distance, Matrix Chain
Multiplication, Longest Increasing Subsequence, Coin Change, Rod Cutting. The premium graph and divide & conquer
families are done too — Bellman-Ford, Floyd-Warshall, Tarjan's, Kosaraju's; Quickselect,
Karatsuba's, Strassen's. The premium misc-algorithms and pathfinding families are complete as well
— Kadane's, Prefix Sum, Difference Array, Sliding Window, Two Pointer, Top-K Elements, Reservoir
Sampling, Monte Carlo, Mo's; A* Search, D* Algorithm.

The premium Data Structures are now authored too — advanced (segment_tree, fenwick_tree,
disjoint_set, avl_red_black_tree, b_tree, suffix_tree, skip_list), specialized (bloom_filter,
lru_cache, kd_tree, graph_variants), and the ADTs (list, set, map, priority_queue).

**Phase 2 content is complete: all 81 Data Structures + Algorithms topics now have full 7-section
content, verified registered in `TopicContentProvider`.** Nothing in the DSA browser falls through
to ComingSoon anymore. (Simulations remain `NotYetAvailable` for topics without a built lab — that
belongs to Phase 3, not this content pass.) Remaining topic-content work is the AI-mode taxonomy
(Phase 5) and Interview Prep sets (Phase 6), both tracked in their own phase docs.

Analysis and Interview Prep topic *content* is out of scope entirely for now — those belong to
Phase 4 (Algorithm Analysis Module) and Phase 6 (Interview Prep Module) respectively. This phase
only gave them browser chrome + row metadata so all five tabs are visually consistent.
