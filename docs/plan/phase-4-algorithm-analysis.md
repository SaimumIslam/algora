# Phase 4 — Algorithm Analysis Module

Status: 2 flagship tools done — remaining 6 deferred
Depends on: Phase 3

## Goal
Build the Analysis tab tools: operation counting, profiling, and growth visualization.

## Rough scope
- Operation counter tied to live code execution
- Cost/runtime profiler
- Parameterized input generator (vary n, distribution, etc.)
- Growth-curve chart (log/linear/exponential overlays)
- Best-case vs worst-case side-by-side comparison
- Benchmark dashboard — real vs predicted performance charts
- Amortized analysis walkthrough (e.g. dynamic array resizing)
- Sandbox mode for free-form experimentation

## What actually got built (this session)

The design mock has zero precedent for any Analysis tool (no wireframe, no chart shape — unlike
Phase 3's linked-list/graph sims). This is net-new design. Scoped to 2 flagship tools proving two
distinct widget shapes:

- **Operation Counter** (`feature/analysis/tools/OperationCounterTool.kt`) — genuinely "tied to
  live code execution": real `linearSearchCounted`/`binarySearchCounted` Kotlin functions run
  against a freshly generated sorted array on every "Run" tap, searching for a value that's never
  present so each run demonstrates the true worst-case bound. The displayed count is what that
  code actually executed, not a formula. Verified on-device: n=16 gives exactly 16 comparisons for
  Linear Search and exactly 4 for Binary Search (log₂16).
- **Growth Curve Chart** (`feature/analysis/tools/GrowthCurveChartTool.kt`) — Canvas line chart
  overlaying O(1)/O(log n)/O(n)/O(n log n)/O(n²) on a log-scaled y-axis (so the quadratic curve
  doesn't flatten the others), with a `maxN` slider and a legend showing each class's live
  computed value at the current `maxN`.

Both bypass the standard 7-section educational template entirely (`TopicDetailScreen.kt` now has
an `AnalysisToolRegistry` check before the `content == null` fallback) — they're dashboards, not
prose topics, per `features.md` §3.3's own framing. They still share the standard `DetailHeader`
and "mark as complete" checkbox, so they still count toward the Analysis tab's progress bar.

## Deferred to a later session
Cost/runtime profiler, parameterized input generator, best/worst-case comparison, benchmark
dashboard (real vs predicted), amortized analysis walkthrough, sandbox mode — all still fall
through to the "coming soon" placeholder. None have any mock precedent either; build each once its
own design is worked out, following `AnalysisToolRegistry`'s pattern (add a topic id → composable
entry, no `TopicContent` needed).
