# Phase 6 — Interview Prep Module

Status: In progress
Depends on: Phase 2 (links back to Algorithms topics)

## Goal
Build the Interview Prep tab: practice problems tagged by pattern and company, cross-linked to Algorithms content, plus a timed quiz mode.

## Rough scope
- Mock question sets
- Company tagging
- Pattern tagging (sliding window, two pointer, etc.)
- Cross-links back to relevant Algorithms topic pages
- Timed quiz mode

## Detail plan (this session)

### Pattern guides (reuse the 7-section template)
The `Patterns` category topics (Sliding Window, Two Pointer, Fast & Slow Pointers, …) are prose learning units — they fit the existing `TopicContent` template exactly. Author full content for the flagship few, each with `crossLinks` to the matching Algorithms topic (e.g. Sliding Window Pattern → `sliding_window`). Sim = NotYetAvailable (patterns aren't visualized). Register in `TopicContentProvider`.

### Timed quiz mode (new unit type)
- `quiz/QuizModels.kt` — `QuizQuestion` (prompt, options, correctIndex, `patternTag`, `companyTag?`, difficulty, explanation, optional cross-link to an Algorithms topic) and `Quiz` (title, description, `timeLimitSeconds`, questions).
- `quiz/QuizContent.kt` — the authored question banks.
- `quiz/QuizRegistry.kt` — maps a topic id → `Quiz` (same pattern as `AnalysisToolRegistry`).
- `quiz/QuizScreen.kt` — paginated one-question-at-a-time flow with a live countdown timer (auto-submits on expiry), selectable option cards, then a results view: score, per-question review (your answer vs correct + explanation), and a tappable cross-link chip to the related Algorithms topic. Retry resets. Marks the topic complete on finish.
- `TopicDetailScreen` resolves `QuizRegistry.get(id)` before the content/tool branches and hands off to `QuizScreen`.

### Flagship content shipped
- Pattern guides: **Sliding Window Pattern**, **Two Pointer Pattern**, **Fast & Slow Pointers** (full 7-section + cross-links).
- Quizzes: **Timed Mock Interview** (free, mixed patterns, 5 min) and **FAANG Set** (company-tagged). `timed_mock_interview` flipped to free so the flagship is cleanly reachable.

### Design fidelity
Quiz chrome reuses the mock's tokens — Surface radii, `SimColors` action buttons, accent tag chips, `TakeawayGreen` for correct answers, a detail-style back header + a timer pill.

## Deferred
System Design Primer; a per-topic problem list separate from quizzes.

## Remaining pattern guides (this session)
`merge_intervals_pattern` (→ merge_sort, timed mock) and `top_k_pattern` (→ top_k_elements, heap,
FAANG set) authored on the standard 7-section template and registered in `TopicContentProvider`.
All five Patterns topics now have full content. Verified on-device.

## Remaining quizzes + behavioral (this session)

- **Company quizzes** — `startupSet` (Stripe/Airbnb/DoorDash/Notion/Databricks) and
  `financeTradingSet` (Two Sigma/Jane Street/Jump/Citadel/HRT), five company-tagged questions each,
  cross-linked to Algorithms topics, added to `QuizRegistry`. Reuse the existing `QuizScreen`.
- **Behavioral bank** — `behavioral/` package: `BehavioralModels` (prompt + category + assesses +
  starTip), `BehavioralContent` (8 prompts across Conflict/Failure/Leadership/…), `BehavioralRegistry`,
  and `BehavioralScreen` — a STAR-framework primer card plus expandable question cards showing "what
  they're assessing" and "how to frame it". Resolved in `TopicDetailScreen` (new branch after the
  quiz branch); "Mark as reviewed" marks the topic complete. Verified on-device.
