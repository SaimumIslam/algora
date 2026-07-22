# Phase 4 — Algorithm Analysis Module

Status: DONE — all 17 Analysis tool ids built (2 flagship + Step 0 infra + sub-phases A/B/C/D)
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

---

## Deferred build-out plan (expanded)

`AnalysisTopics.kt` actually declares **17 topics; 2 are built** (`operation_counter`,
`growth_curve_chart`). The remaining **15 ids fall through to the "coming soon" placeholder**. The
roadmap's "6 deferred" is a conceptual grouping — the registry gap is finer-grained. The 15 ids
collapse to ~11 distinct tools (the 3 case ids and the 3 empirical ids each share one composable).

**Design constraint (unchanged):** the mock has zero Analysis precedent, so every tool is net-new
design. Reuse the established pattern: `Surface`(18dp radius, `outline` border, 16dp pad) + intro
text + `Slider` + real instrumented Kotlin + `Canvas`. Register via `AnalysisToolRegistry` (`id →
@Composable`); no `TopicContent`. Tools still get `DetailHeader` + "mark complete" checkbox, so
they count toward the Analysis progress bar. No nav changes — every topic id is already routed.

### Step 0 — Shared infra first (kills the duplication both flagship tools already carry)

| File | What |
|---|---|
| `tools/common/AnalysisToolScaffold.kt` | Extract the repeated `Surface` card + intro-text slot. Refactor `OperationCounterTool` + `GrowthCurveChartTool` onto it. |
| `tools/common/InstrumentedAlgos.kt` | Single home for real counted code. Move `linear/binarySearchCounted` out of `OperationCounterTool`; add `insertionSortCounted`, `quicksortCounted`, `bubbleSortCounted`, `twoSumBrute/Hash`, `dynamicArrayPush`. Deterministic op counts = testable. |
| `tools/common/ComplexityCurves.kt` | Extract the `Curve` list + fns from `GrowthCurveChartTool`. Reused by growth wrappers, master theorem, real-vs-predicted overlay. |
| `tools/common/MiniCharts.kt` | Generalize `GrowthCanvas` → `MiniLineChart(series)` + `MiniBarChart(bars)`, with a log/linear y-axis flag. |

### Step 1 — Cheap deterministic tools (sub-phase A)

| Topic id(s) | Tool | Design |
|---|---|---|
| `best_case` `worst_case` `average_case` | **one** `CaseAnalysisTool(mode)` | Same algo (insertion sort), 3 crafted inputs. Shows real op count + which input triggers the bound. 3 registry entries → same composable, different default tab. |
| `growth_class_comparison` | `GrowthClassComparisonTool` | Thin wrapper on `MiniLineChart` + log/linear toggle. |
| `complexity_class_comparison` | `ComplexityClassComparisonTool` | Table of classes with show/hide checkboxes + live n-value column. |

### Step 2 — Instrumented tools (sub-phase B)

| Topic id | Tool | Design |
|---|---|---|
| `cost_profiler` | `CostProfilerTool` | Sweep n across a range, run instrumented algo each, plot ops-vs-n bars + table. |
| `parameterized_input_generator` | `InputGeneratorTool` | Size slider + distribution dropdown (sorted/reversed/random/nearly-sorted/dupes). Preview array + feed a chosen algo → show how input shape moves op count. |
| `amortized_analysis` | `AmortizedAnalysisTool` | Dynamic-array push with doubling resize. Step/play transport (reuse Phase 3 playback idea). Per-op cost spikes at resize + running average converging to O(1). |
| `space_time_tradeoffs` | `SpaceTimeTradeoffTool` | Two-sum brute vs hashmap. Paired bars: ops vs extra memory. |

### Step 3 — Empirical timing trio (sub-phase C — needs care)

| Topic id(s) | Tool | Caveat |
|---|---|---|
| `benchmark_dashboard` `real_vs_predicted_runtime` `input_size_scaling` | **one** `BenchmarkTool(variant)` | Wall-clock via `System.nanoTime()` across an n sweep. **On-device timing is noisy** → average repeated trials + a warmup run. Overlay predicted Big-O fitted to the first point. 3 variants = same tool, different default view. |

### Step 4 — Remaining exploration / reference (sub-phase D)

| Topic id | Tool | Design |
|---|---|---|
| `master_theorem` | `MasterTheoremTool` | Pure calculator. a, b, f(n) steppers → case 1/2/3 + Θ result + worked examples. No timing. |
| `complexity_map` | `ComplexityMapTool` | Algorithms as chips grouped into complexity-band rows (O(1) … O(2ⁿ)). |
| `sandbox_mode` | `SandboxTool` | Composes the primitives: pick algo + input params + run, accumulate results into a shared chart. |

### Registry wiring

Add 15 entries to `AnalysisToolRegistry.tools`. Shared tools map several ids to one composable with an arg:

```kotlin
"best_case"    to { CaseAnalysisTool(CaseMode.BEST) },
"worst_case"   to { CaseAnalysisTool(CaseMode.WORST) },
"average_case" to { CaseAnalysisTool(CaseMode.AVERAGE) },
```

### Verification

- `./gradlew assembleDebug`.
- Deterministic tools (Steps 1–2, master theorem): assert exact op counts (e.g. insertion sort worst-case on reversed n=8 = 28 comparisons).
- Timing tools (Step 3): manual on-device; verify curve *shape*, not absolute ms.

### Landed so far

- **Step 0** — `tools/common/`: `AnalysisToolCard` (shared card chrome), `InstrumentedAlgos`
  (real counted search/sorts/two-sum/dynamic-array-push), `ComplexityCurves` (`Curve` +
  `complexityCurves`), `MiniCharts` (`MiniLineChart`, `MiniBarChart`). Both flagship tools
  refactored onto the scaffold + shared curves/chart.
- **Sub-phase A** — `CaseAnalysisTool(mode)` (3 registry ids: `best_case`/`worst_case`/
  `average_case`, insertion sort on crafted inputs, all-three breakdown at current n),
  `GrowthClassComparisonTool` (log/linear toggle), `ComplexityClassComparisonTool`
  (per-class show/hide checkboxes). 5 registry entries added; `./gradlew assembleDebug` green.

- **Sub-phase B** — `CostProfilerTool` (`cost_profiler`; sweeps n, bars + table of measured ops
  for linear search / insertion / bubble on worst-case input), `InputGeneratorTool`
  (`parameterized_input_generator`; 5 distributions → live array preview + insertion-sort
  comparison count), `AmortizedAnalysisTool` (`amortized_analysis`; doubling dynamic-array push,
  per-push cost bars with resize spikes + amortized average), `SpaceTimeTradeoffTool`
  (`space_time_tradeoffs`; two-sum brute vs hash, time/space metric cards). 4 registry entries
  added; `./gradlew assembleDebug` green.

- **Sub-phase C** — `BenchmarkTool(variant)` (one composable, 3 ids: `benchmark_dashboard`/
  `real_vs_predicted_runtime`/`input_size_scaling`). Real `System.nanoTime()` timing of insertion
  sort on reverse-sorted inputs across n = 500…2500, warmup + 4-trial average, run off the main
  thread via `Dispatchers.Default` behind a "Run benchmark" button. Variants share the measured bar
  chart, differ in table: raw µs / measured-vs-predicted-O(n²) / ×-prev scaling ratio. Timing is
  non-deterministic → no op-count assertion; manual on-device read of trend only. `./gradlew
  assembleDebug` green.

- **Sub-phase D** — `MasterTheoremTool` (`master_theorem`; a/b/d steppers, compares d to log_b(a) →
  case 1/2/3 + Θ result, 4 preset recurrences), `ComplexityMapTool` (`complexity_map`; six
  complexity bands green→red, algorithms as chips per band), `SandboxTool` (`sandbox_mode`; pick
  algo × input shape × n, each run's real op count stacks onto a shared bar chart + table, clear).
  3 registry entries added; `./gradlew assembleDebug` green.

### ROADMAP note

`ROADMAP.md` Phase 4 row said "6 more deferred". Real registry gap was 15 topic ids → 11 distinct
tools (3 case ids share `CaseAnalysisTool`, 3 empirical ids share `BenchmarkTool`). **All now
built** — update the Phase 4 row to "Done".
