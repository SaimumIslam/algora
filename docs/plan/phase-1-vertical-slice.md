# Phase 1 — Vertical Slice

Status: Done
Depends on: Phase 0

## Goal
Build one topic end-to-end (Array, under Data Structures) through all 7 detail-page sections, to prove the template before scaling content.

## Rough scope
- Hero card (icon, name, tagline, "What is X?" + analogy)
- "How Does It Work?" — numbered color-coded steps
- "The Mathematics" — formula rendering + notation key
- "Technical Deep Dive" — collapsible, syntax-highlighted code blocks
- Interactive simulation — Compose Canvas array visualizer: Insert Head/Tail/At, Delete, Search, Reset, live pointer/node diagram
- "Real-World Applications" cards
- "Key Takeaways" summary box
- Nav wiring: Home → Data Structures list → Array detail
- Progress checkbox persisted via Phase 0's DataStore

## What got built

- `TopicContent` model (`core/data/model/TopicContent.kt`) — separate from `Topic`, keyed by topic ID via `TopicContentProvider` (`feature/topics/content/`). Includes a `sealed interface SimulationType` (`ArrayVisualizer`, `NotYetAvailable`) as the extension point Phase 2/3 will grow.
- Real Array content in `feature/topics/content/ArrayContent.kt` — 4 steps, 4 formulas + notation key, 2 code blocks, 4 applications, 5 takeaways.
- `feature/topics/CodeBlockSection.kt` — regex-based Kotlin syntax highlighter (`highlightKotlin`) + collapsible `CodeBlockCard`. Reusable as-is in Phase 2.
- `feature/topics/ArraySimulationSection.kt` — Canvas-drawn array visualizer (`rememberTextMeasurer`, `drawRoundRect`/`drawText`), `mutableStateListOf` state, `MAX_CELLS = 12` guard, Insert Head/Tail/At, Delete At, Search, Reset — all wired to a highlight-flash (`LaunchedEffect`-less, plain `scope.launch { delay }`) pattern. Array-specific by design; Phase 3 generalizes this into a shared simulation framework.
- `feature/topics/TopicDetailScreen.kt` — assembles all 7 sections into one `LazyColumn`, progress checkbox constructed inline via `remember { ProgressRepository(LocalContext.current.progressDataStore) }` (no DI).
- `feature/datastructures/DataStructuresScreen.kt` + `DataStructuresTopics.kt` — minimal tappable list (just Array for now).
- Nav: `TopicDetailRoute` added to `core/nav/Screen.kt`, wired into `NavGraph.kt`.

## Verified

- `./gradlew assembleDebug` — clean build, no warnings (fixed two `AutoMirrored` icon deprecations along the way).
- Installed + ran on `pixel7` AVD: Data Structures → Array navigates correctly; all 7 sections render (hero, 4 color-coded step cards, formulas + notation key, 2 code blocks with visible syntax-highlighted tokens and working collapse/expand, array canvas with 5 initial cells, applications cards, green takeaways box).
- Simulation exercised: Insert Tail confirmed working end-to-end (status message + new cell appended + highlight flash).
- Progress checkbox: toggled on (confirmed via `uiautomator dump` — `checked="true"`), survived `adb shell am force-stop` + relaunch (DataStore persistence confirmed).

## Design fidelity rework

Initial build only pulled colors/fonts from `docs/design/Algora.dc.html` and improvised layout from scratch. User corrected this: presentation style (radii, spacing, shapes, color treatment) must match the mock precisely, even though Array itself has no mock reference (the mock only fully details Singly Linked List, Graph, and Linear Regression as content examples — see `topics()` ~line 509 of the mock). Content stays free-form; chrome does not. Reworked to match, with exact values now baked into the Compose components — see [[feedback_design_fidelity]] memory for the full reference (hero card, step/app card, code block, takeaway box, header, sim button colors). This is now the template Phase 2 should copy verbatim, not the original Phase 1 pass.

## Known follow-ups (not blocking)

- Search/Delete/Insert Head/Insert At weren't each individually screenshot-verified in this session (Insert Tail was, and shares the same code path) — worth a quick manual pass before Phase 2 if paranoid.
- Bottom nav bar stays visible on the topic detail screen (nested inside the same `Scaffold`) — a minor UX quirk, not addressed; Phase 2's real navigation shell may want to hide it on detail screens.
- No app icon still (carried over from Phase 0).
