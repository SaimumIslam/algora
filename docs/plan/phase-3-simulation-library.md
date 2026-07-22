# Phase 3 — Interactive Simulation Library

Status: Data-structure sims done — recursion tree, DP grid, generic slider-explorer, gate solver deferred
Depends on: Phase 2

## Goal
Build the reusable simulation widget types referenced across the taxonomy, then attach them to the topics that need them.

## Rough scope
- Linked list / stack / queue visualizer (canvas, operation buttons)
- Graph builder + animated BFS/DFS traversal (state legend: Idle/Current/Visited/Queue/Active)
- Recursion tree / call-stack visualizer w/ playback transport (reset, step-back, play/pause, step-forward, speed slider)
- DP tabulation grid — animated cell-by-cell fill + traceback highlighting
- Generic slider-driven parameter explorer (reused later for e.g. linear regression)
- Classifier "gate solver" (AND/OR/XOR) w/ weight/bias sliders and pass/fail detection
- Wire each sim into its target topics from Phase 2's placeholders

## What actually got built (this session)

Research confirmed only linked list, graph+BFS/DFS, and a slider-driven explorer (regression — an
AI-mode widget) are actually specified anywhere (mock or `docs/features.md`); recursion-tree/DP
grid/gate solver have zero spec and no current DSA topic needs them. Scoped this session to the
4 widgets that are mock-specified (or a close extension of the existing `ArraySimulationSection`
pattern) **and** have a real, already-authored Phase 2 topic waiting for them:

- `LinkedListSimulationSection.kt` — mirrors the mock's `simLinked` exactly: box-chain visual,
  violet/amber/green node states, animated 420ms/step linear search walk. Wired to
  `singly_linked_list`.
- `StackSimulationSection.kt` / `QueueSimulationSection.kt` — not mock-specified (mock never built
  a Stack/Queue topic), designed to match the same visual language (box gradients, `SimColors`
  palette). Wired to `stack` / `queue`.
- `GraphSimulationSection.kt` — mirrors the mock's `simGraph`: circular node layout (not
  force-directed), Add Node/Link controls, BFS/DFS as a precomputed snapshot sequence played back
  at 600ms/tick, 4-swatch legend. Wired to both `graph` and `bfs` (BFS topic reuses the same
  graph-builder-plus-traversal widget rather than getting a separate single-purpose one).

`core/ui/theme/Color.kt` gained a `SimColors` object consolidating the `btn*` palette that was
previously duplicated as private vals in `ArraySimulationSection.kt` — all 4 new widgets and Array
now share it. `SimulationType` (in `TopicContent.kt`) is a sealed interface with one `data object`
per widget; `TopicDetailScreen.kt`'s simulation branch is an exhaustive `when`, so a future widget
type is a compiler-enforced reminder to wire it in.

All 5 wired topics (Array, Singly Linked List, Stack, Queue, Graph, BFS) were manually verified on
an emulator: insert/delete/search on the linked list (including the animated found-highlight), and
add-node/link/BFS/DFS on the graph (confirmed node colors animate through queue→current→visited
and status/counters update correctly, ending at "Complete").

## Deferred to a later session
Recursion-tree/call-stack player (needs full playback transport — reset/step-back/play/pause/
step-forward/speed-slider — richer than anything currently built), DP tabulation grid (needs a
concrete DP problem chosen to visualize), the generic slider-driven parameter explorer, and the
AND/OR/XOR gate solver. None of these have a current DSA topic to attach to (the two AI-mode ones
belong with Phase 5); build them once their target topics exist or their design is worked out from
scratch, since the mock provides no reference for any of the four.
