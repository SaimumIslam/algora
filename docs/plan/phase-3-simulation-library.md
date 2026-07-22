# Phase 3 — Interactive Simulation Library

Status: DONE — data-structure sims + gate solver + ClassifierPlayground + PlaybackTransport + recursion-tree (factorial/fibonacci/hanoi/n-queens) + DP grid (fibonacci_dp/edit_distance/LCS/coin_change/knapsack_01); slider-explorer subsumed
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
concrete DP problem chosen to visualize), and the generic slider-driven parameter explorer. None
of these have a current DSA topic to attach to; build them once their target topics exist or their
design is worked out from scratch, since the mock provides no reference.

> **Stale note.** The "no current topic to attach to" claim is no longer true — see the build plan
> at the bottom of this file. Recursion and DP topics already exist on `NotYetAvailable`.

## Gate solver — status correction (this note supersedes the deferral above)

The **AND/OR/XOR gate solver is built and shipped.** Phase 5 implemented it as
`PerceptronSimulationSection.kt` — its own header reads *"the classifier-gate solver from the Phase
3 scope, applied to The Perceptron."* It has the full gate selector, w₁/w₂/bias sliders,
decision-boundary canvas, truth table, and pass/fail detection (`correct/4` + XOR-not-separable
message). Wired as `SimulationType.PerceptronVisualizer` → attached to the `perceptron` topic
(`PerceptronContent.kt`). On-device verified in Phase 5.

What's **not** done is the Phase 3 *intent*: a **reusable** widget (its own `SimulationType`)
attachable to multiple topics. It's currently perceptron-specific, and ~150 topics still sit on
`SimulationType.NotYetAvailable`. Plan below generalizes it.

## Generalization plan — reusable ClassifierPlayground

Goal: extract the gate solver into a config-driven `ClassifierPlaygroundSection`, then reuse it for
other linear-classifier topics. Perceptron's current on-screen view must stay byte-identical (it's
already verified — don't regress it).

### Step 0 — Extract the core
- Pull the gate/weights/bias/canvas/truth-table logic out of `PerceptronSimulationSection` into a
  new `feature/topics/ClassifierPlaygroundSection.kt`.
- Config = data class: dataset (list of points + target labels), classifier fn (params → predicted
  label), slider specs (label + range), axis ranges, success message, optional teaching note.
- Rewrite `PerceptronSimulationSection()` as a thin wrapper that calls `ClassifierPlaygroundSection`
  with the AND/OR/XOR gate config. Zero visual change to the perceptron screen.
- Reuse the existing `Surface`(18dp radius, `outline` border) chrome + `SimColors` palette already
  in that file.

### Step 1 — New SimulationType + exhaustive wiring
- Add `data object ClassifierPlayground : SimulationType` to the sealed interface (`TopicContent.kt`).
- `TopicDetailScreen.kt`'s `when` is exhaustive → compiler forces the new branch.
- Add the label to `SimulationsScreen.kt`'s map (e.g. "Classifier playground").

### Step 2 — Config resolution (the one architectural choice)
`SimulationType` entries are param-free `data object`s, so the type can't carry per-topic config.
Resolve config by **topicId**, mirroring `AnalysisToolRegistry`'s id→composable pattern: a
`classifierConfigFor(topicId): ClassifierConfig` map in the section file. Keeps `SimulationType`
param-free and consistent with how every other sim resolves (no args). (Alternative — make it
`data class ClassifierPlayground(val configId: String)` — is rejected: it breaks the param-free
convention and the `data object` equality the `when` relies on.)

### Step 3 — Configs + attach to reuse targets
- **Logistic Regression** (`logistic_regression`): two gaussian blobs, sigmoid boundary, weight/bias
  sliders; success = accuracy over a threshold.
- **SVM** (`svm`): two blobs, linear boundary + margin lines; w/b sliders; surface the margin width.
- **Perceptron** (`perceptron`): existing gate config, unchanged.
- Flip these topics' `simulation =` from `NotYetAvailable` to `ClassifierPlayground`.
- **KNN / Decision Trees are a poor fit** (non-linear / non-parametric — no single tunable straight
  boundary). Skip them for this widget; they'd need a separate sim. Don't force them in.

### Step 4 — Verify
- `./gradlew assembleDebug`.
- Emulator: perceptron screen pixel-unchanged (regression check); logistic_regression + svm render
  their blobs with a tunable decision boundary and live accuracy/margin readout.

---

## Build plan — remaining sims (recursion tree + DP grid)

The two remaining sim widgets **do** have target topics now (the deferral note above is stale, same
pattern as the gate solver). Confirmed ids on `NotYetAvailable`:

- **Recursion tree / call-stack:** `factorial`, `fibonacci_recursive`, `tower_of_hanoi`, `n_queens`
- **DP tabulation grid:** `fibonacci_dp`, `coin_change`, `knapsack_01`, `edit_distance`,
  `longest_common_subsequence`, `rod_cutting`, `matrix_chain_multiplication`,
  `longest_increasing_subsequence`

Two reusable widgets light up ~12 dead topics. Both follow the conventions already in the codebase:
**precomputed-snapshot playback** (like `GraphSimulationSection`'s BFS/DFS sequence) and
**config-resolved-by-topicId** (like `ClassifierPlayground` / `AnalysisToolRegistry`). Reuse the
`Surface`(18dp radius / `outline` border) chrome and `SimColors`.

### Step 0 — Shared playback transport (Phase 3 scope item, still unbuilt)
- Add `PlaybackTransport` to `SimComponents.kt`: reset ⏮ · step-back · play/pause · step-forward ⏭ +
  speed slider. Auto-advance driven by a `LaunchedEffect(playing, speed)` tick.
- Model: a sim is a `List<Step>` + a current index. `PlaybackTransport` owns index/playing state and
  calls back on change; the widget renders `steps[index]`. Both new widgets (and any future stepped
  sim) share it.

### Sub-phase A — Recursion tree / call-stack (`RecursionTreeVisualizer`)
- New `RecursionTreeSection.kt`. Config = `RecursionConfig(nRange, buildSteps: (n) -> List<RecStep>)`.
  A `RecStep` snapshots: active call, the call stack (list), and each tree node's state
  (pending / active / returned + return value).
- Canvas: tree laid out by depth × sibling order (precomputed, not force-directed — mirror the graph
  sim's fixed-layout choice); a call-stack column beside it; current return value.
- Ship **`factorial`** (linear chain) and **`fibonacci_recursive`** (binary tree, cap n ≤ 6 so the
  tree stays legible) first — clean, small trees.
- `tower_of_hanoi` / `n_queens` trees explode; defer to sub-phase C with a hard input cap (e.g. 3
  disks, 4×4 board) or attach later.

### Sub-phase B — DP tabulation grid (`DpGridVisualizer`)
- New `DpGridSection.kt`. Config = `DpConfig(rows, cols, axisLabels, buildFill: -> List<CellFill>,
  traceback: List<Cell>, cellLabel)`. `CellFill` = (r, c, value); fill order is the DP recurrence
  order.
- Canvas/grid: animate cell-by-cell fill via `PlaybackTransport`; after the last fill step, highlight
  the traceback path.
- Ship **`fibonacci_dp`** (1-D row) and **`edit_distance`** (classic 2-D with traceback) first; then
  `longest_common_subsequence`, `coin_change`, `knapsack_01` via added configs. 1-D problems render
  as a single row, 2-D as a matrix — same widget.

### Sub-phase C (stretch) — capped recursion trees
- `tower_of_hanoi` (3 disks), `n_queens` (4×4) with hard input caps so the backtracking tree stays
  renderable. Reuse sub-phase A's widget + transport.

### Wiring (each sub-phase)
- Add the `SimulationType` data object(s) — exhaustive `when` in `TopicDetailScreen` is
  compiler-enforced. Add `SimulationsScreen` labels.
- `recursionConfigFor(topicId)` / `dpConfigFor(topicId)` registries.
- Flip each shipped topic's `simulation =` off `NotYetAvailable`.

### Generic slider-explorer — close as subsumed
The third deferred item is **already covered** by `RegressionExplorer` and `ClassifierPlayground`
— both are slider-driven parameter explorers. No distinct target topic needs a separate generic
explorer. Recommend marking it done-by-reuse unless a non-classifier slider target appears; don't
build a fourth near-duplicate.

### Verify
- `./gradlew assembleDebug`.
- Emulator: factorial recursion plays through with working transport (reset / step / play / speed);
  edit_distance grid fills cell-by-cell then highlights the traceback.
