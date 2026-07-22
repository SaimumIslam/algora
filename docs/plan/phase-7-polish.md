# Phase 7 — Engagement & Polish

Status: In progress
Depends on: Phases 1-6 (touches most of the app)

## Goal
Retention/engagement features layered on top of the finished content + nav.

## Rough scope
- Dark mode
- Bookmarks / "continue where left off"
- Spaced-repetition review scheduler
- Quiz/flashcard mode auto-generated from each topic's Key Takeaways
- Difficulty tags + prerequisite graph UI
- Multi-language code snippet toggle (Python/Java/JS/C++) in Technical Deep Dive
- Local streaks (device-local per Phase 0 decision)

## Detail plan (this session)

### Settings persistence
New `settings` DataStore (separate from progress) + `SettingsRepository`: theme mode (SYSTEM/LIGHT/DARK), bookmark ids, last-opened topic id, and a local streak (count + last-active epoch-day). Every screen instantiates the repo from `LocalContext`, so all share one store and react to each other's writes — no state threading.

### Manual dark mode (persisted)
`AlgoraTheme` already accepts `darkTheme`. `MainActivity` collects the stored theme mode and resolves it (SYSTEM → follow OS). The mock's topbar sun/moon button on Home flips LIGHT ⇄ DARK and persists.

### Local streaks
On launch, `recordActivityToday()` bumps the streak (consecutive day → +1, gap → reset to 1). Rendered as the mock's flame pill in the Home topbar.

### Bookmarks + continue where left off
Opening any topic records it as last-opened. The content detail header gets a bookmark star. Home gains a "Jump back in" card (last-opened) and a "Bookmarks" list.

### Flashcards from Key Takeaways
`TopicContentProvider` exposes its entries; `FlashcardScreen` flattens every authored topic's `takeaways` into flip cards (front = topic + prompt, back = the takeaway), shuffled, with prev/next. Reached from a Home "Review Flashcards" button (own nav route, no bottom bar).

### Difficulty tags
`Topic.difficulty` already exists — surface it as a chip on `TopicRow` and the detail hero where set.

### Multi-language code toggle
`CodeBlock` gains optional `variants: List<CodeVariant>` (language + code). When present, `CodeBlockCard` shows a language segmented toggle and renders the selected variant; otherwise the single Kotlin snippet as before. Populated for flagship topics (Array, Linear Regression) with Python/Java/JavaScript.

## Deferred
Multi-language snippets for the remaining topics; bookmark/streak analytics.

## Spaced repetition + prerequisite graph (this session)

- **SM-2 spaced repetition** — `SrsCard` + `sm2()` implement the standard SM-2 update (quality 0-5,
  <3 lapses). Per-card state (`reps|ef|interval|due`) persists in the settings DataStore under one
  `SRS` set key. `SettingsRepository.srs`/`reviewCard()`. New `ReviewScreen` builds the due-today
  queue from every authored topic's takeaways (keyed `topicId#index`), flips to reveal, grades
  Again/Good/Easy, reschedules + advances. Reached from a Home "Spaced Repetition" card
  (`review` route). Distinct from the existing shuffle-all `FlashcardScreen`. Verified on-device.
- **Prerequisite graph UI** — `PrerequisiteGraph`, a curated learning-order DAG over the core DSA
  backbone (`prereqsOf` / `unlockedBy` reverse edges). `TopicDetailScreen` gains a "Learning Path"
  section rendering tappable "Learn first" + "Unlocks" chips that navigate. Renders nothing for
  topics outside the curated graph. Verified on-device (coin_change → Fibonacci-DP / 0/1-Knapsack).
