# Phase 0 — Foundation & App Shell

Status: Done
Depends on: none

## Goal
Stand up the Android project skeleton so Phase 1 has somewhere to put a real screen.

## Rough scope
- Android Studio/Gradle project, Kotlin + Jetpack Compose
- Feature-based package structure (core/ui, core/data, feature/topics, feature/simulation, ...)
- Theme from design mock: Space Grotesk (headers) + IBM Plex Sans (body) + IBM Plex Mono (code), light/dark base color tokens
- Bottom nav scaffold — DSA mode: Interview Prep · Data Structures · Home · Algorithms · Analysis (tabs can be stubs)
- Local data models: `Topic`, `Category` as Kotlin data classes
- DataStore wired up for per-topic progress persistence (empty schema is fine for now)

## What got built

- **Stack**: Gradle 9.6.1 (wrapper), AGP 9.3.0 (built-in Kotlin, no separate `kotlin-android` plugin), Kotlin 2.4.10, Compose BOM 2026.06.01, Navigation Compose 2.9.8, DataStore Preferences 1.2.1. `compileSdk`/`targetSdk` 37, `minSdk` 26. `gradle.properties` pins `org.gradle.java.home` to the Android Studio JBR (21.0.10) so CLI and IDE builds use the same JVM; `kotlin { jvmToolchain(17) }` sets the compile target.
- **Single Gradle module** (`:app`), package-separated: `core/ui/theme`, `core/data/model`, `core/data/progress`, `core/nav`, `feature/{home,datastructures,algorithms,analysis,interviewprep}`.
- **Theme**: `Color.kt`/`Typography.kt`/`Theme.kt` under `core/ui/theme`. Space Grotesk and IBM Plex Sans are bundled as their upstream *variable* fonts (single `.ttf` each, from `google/fonts`) and instantiated per-weight via Compose's `FontVariation.Settings` API (`@OptIn(ExperimentalTextApi::class)`) rather than separate static files. IBM Plex Mono uses static Regular/Medium `.ttf`s. Light/dark `ColorScheme`s built from the design mock's hex palette (`docs/design/Algora.dc.html`); `CategoryAccents` holds the 8 category accent colors for later use in category badges.
- **Nav**: `Screen.kt` sealed class + `dsaTabs` list, `NavGraph.kt` (`NavHost`, Home as start destination), `MainActivity.kt` wires `Scaffold` + `NavigationBar` + the 5 stub screens. Uses `material-icons-extended` for tab icons (core icon set didn't have Analytics/Functions/MenuBook/School).
- **Data models**: `Topic`/`Category`/`Section`/`Difficulty` in `core/data/model` — deliberately flat, no content-section fields yet (those are Phase 1's problem once the vertical slice defines what a simulation config needs).
- **DataStore**: Preferences DataStore (not Proto — schema is one `Set<String>` for now), `ProgressRepository` with `Flow<Set<String>>` + suspend mark complete/incomplete, unit-tested via `PreferenceDataStoreFactory` + `TemporaryFolder` (no Android instrumentation needed).

## Verified

- `./gradlew assembleDebug` — BUILD SUCCESSFUL
- `./gradlew testDebugUnitTest` — 2/2 `ProgressRepositoryTest` cases pass
- Installed + ran on `pixel7` AVD (API from installed system image): dark theme renders correctly (mock's `#161a22` bg, indigo accent), Space Grotesk headline font visibly distinct, all 5 bottom-nav tabs (Interview Prep · Data Structures · Home · Algorithms · Analysis) navigate correctly and highlight the active tab.

## Known follow-ups (not blocking, noted for later phases)

- No app icon set (default launcher icon) — cosmetic, revisit whenever the design mock's icon asset is exported.
- No light-mode visual check was done on-device (dark mode confirmed only) — worth a quick check before Phase 1 if system-theme switching matters early.
