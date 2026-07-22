# Phase 5 — AI Mode

Status: In progress
Depends on: Phase 3 (reuses sim widgets)

## Goal
Add the second app mode (AI: ML/DL/NLP/RL) alongside DSA mode, sharing the same topic template and nav pattern.

## Rough scope
- DSA/AI mode switch
- AI bottom nav: Machine Learning · Deep Learning · Home · NLP · Reinforcement Learning
- Topic content for ML, DL, NLP, RL taxonomies (see `docs/features.md` §2.4-2.7) via the same 7-section template
- Reuse Phase 3 sim widgets where applicable (e.g. gate solver → perceptron, slider explorer → regression)
- Cross-links between DSA and AI topics (e.g. Graph traversal ↔ MCTS, Gradient Descent ↔ backprop)

## Detail plan (this session)

### Mode infrastructure
- `AppMode` enum (DSA, AI) hoisted in `AlgoraApp` (MainActivity). Bottom bar swaps `dsaTabs` ⇄ `aiTabs`.
- `Screen` gains `MachineLearning`, `DeepLearning`, `Nlp`, `ReinforcementLearning` (short labels ML/DL/NLP/RL to fit a 5-tab bar). `aiTabs = [ML, DL, Home, NLP, RL]`.
- `NavGraph` registers the four AI category routes and passes `mode`/`onModeChange` into Home; `TopicDetailScreen` gains `onTopicClick` for cross-links.

### Home dashboard (now shared by both modes — hosts the switch)
Ported from the mock's `isHome` block: gradient topbar (DSA gradient / AI pink→orange gradient) with the DSA / AI Simulation segmented switch, a Featured Lab card (DSA → Singly Linked List, AI → Linear Regression), and a 2×2 Quick Access card grid that deep-links into the four category tabs of the active mode.

### AI content (mirrors DSA feature packages)
`feature/{machinelearning,deeplearning,nlp,reinforcementlearning}/` each with `*Categories`, `*Topics`, `*Screen`. Categories = the mock's `cats()` sub-sections; topic rows + free/premium flags taken verbatim from the mock. `TopicRegistry` aggregates all AI topics so any id resolves.

### Simulations (Phase 3 widgets, built here since deferred earlier)
- `RegressionSimulationSection` — full port of the mock's regression lab (slope/intercept sliders, scatter + best-fit line on Canvas, live MSE, New Data / Best Fit buttons via least-squares).
- `PerceptronSimulationSection` — logic-gate solver (AND/OR/XOR), adjustable weights + bias, live truth-table classification, decision-boundary Canvas, XOR-not-linearly-separable note.

### Flagship content
`linear_regression` (verbatim from the mock's `regression` topic) and `perceptron` (authored) get full 7-section content + their sims. Remaining AI topics ship as browser rows → ComingSoon, matching the Phase 2/3/4 "flagship few, rest deferred" precedent.

### Cross-links
`TopicContent.crossLinks: List<CrossLink>` (optional). Rendered as tappable chips under Key Takeaways. Seeded: Linear Regression ↔ The Perceptron, Graph → Monte Carlo Tree Search.

## Deferred
Full 7-section content for the remaining ~40 AI topics; perceptron multi-layer / backprop sim; per-mode theming of the whole shell (only the Home topbar gradient switches today).
