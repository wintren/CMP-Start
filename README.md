# CMP_Start

A Kotlin Multiplatform / Compose Multiplatform starting point. Android, iOS, Desktop (JVM) and
Web (WasmJS) from one codebase, with a small opinionated architecture and the plumbing already
wired: DI, navigation, theming, a data layer against a real API, and tests that enforce the rules.

The demo app ranks the next seven days across your saved cities by how well the weather suits an
activity. It exists to exercise the parts a template has to prove — domain logic worth testing, a
data layer with real mapping, navigation with a detail screen, and a promoted feature module.
Delete it and keep the scaffolding.

## Stack

| | |
|---|---|
| Kotlin | 2.4.20 |
| Gradle / AGP | 9.7.1 / 9.4.0 (JVM toolchain 21) |
| Compose Multiplatform | 1.12.0 |
| Navigation | Navigation 3 (`NavDisplay` + `entryProvider`) |
| DI | Koin 4.2.2 |
| Networking | Ktor 3.5.2 + kotlinx.serialization |
| Storage | SQLDelight 2.3.2 (+ multiplatform-settings for preferences) |
| Images | Coil 3.5.0, on the app's own Ktor client |
| Localisation | Compose Resources — English, Swedish, Spanish |
| Logging | `Log` in `:core:common` — lazy messages, per-platform call site and sink |
| Tests | kotlin-test, coroutines-test, Ktor MockEngine |

No mocking library and no test compiler plugins. Both are Kotlin-version-locked and would gate
every Kotlin upgrade; hand-written fakes (`app/src/commonTest/.../fake/`) and `MockEngine` cover it.

## Run it

```bash
./gradlew :launch:desktop:run                  # Desktop
./gradlew :launch:desktop:run -PappCatalog     # Design-system catalog
./gradlew :launch:android:installDebug         # Android
./gradlew :launch:web:wasmJsBrowserDevelopmentRun   # Web, at localhost:8081
./gradlew :launch:ios:linkDebugFrameworkIosSimulatorArm64   # iOS framework
```

iOS produces `AppFramework.framework` — add it to an Xcode project and return
`MainViewControllerKt.MainViewController()` from a `UIViewControllerRepresentable`. There is no
`.xcodeproj` in the repo, on purpose: an Xcode project is not something you want to inherit from a
template and then fight.

## Test it

```bash
./gradlew check          # everything below
./gradlew :archtest:test # architecture rules, as tests
./gradlew :domain:desktopTest :data:desktopTest :app:desktopTest :core:common:desktopTest
```

What the tests are there to demonstrate, one each:

| Test | Shows |
|---|---|
| `domain/…/ScoreDayComfortTest` | pure logic needs no fakes, no dispatcher, no rule |
| `data/…/ForecastMapperTest` | mapping a real captured API payload, including ragged arrays |
| `data/…/ForecastRepositoryImplTest` | the data path end to end with `MockEngine` |
| `app/…/BestDayViewModelTest` | a `StateViewModel` with fakes — including the subscription trap |
| `app/…/AppModulesTest` | the Koin graph really constructs, so a missing binding fails the build |
| `data/…/SavedLocationMigrationTest` | a v1 database migrated to v2 with its rows intact |
| `core/…/LogTest` | a filtered log level never invokes the message lambda |
| `archtest/…` | the layering, design-system and string rules, enforced rather than documented |

## Make it yours

```bash
./scripts/rename.sh com.acme.tracker Tracker
```

Rewrites the base package (`com.template`), the Gradle root project name, the Android
`applicationId` and the display name, and moves the source directories. Review the diff, then:

1. Delete the `weather` packages in `:app`, `:domain` and `:data`, and the demo strings.
2. Point `:data` at your own API, or delete `:data` entirely if you have no backend yet.
3. Replace the palette in `design/…/theme/AppColors.kt` and the type scale in `AppTypography.kt`,
   then check both against `./gradlew :launch:desktop:run -PappCatalog`.
4. Prune `values*/strings.xml` in `:app` down to what you keep, in all three locales.
5. Rename the `savedLocation` table in `data/…/sqldelight/` and delete `migrations/1.sqm` — nothing
   is installed yet, so the schema can start at version 1 again.
6. Keep or delete `:feature:settings` — read the note in `docs/architecture.md` first.

## Layout

```
app/                screens, ViewModels, navigation, catalog  (all targets, no artifact)
launch/android      Application + Activity
launch/desktop      main() + window
launch/web          main() + index.html
launch/ios          framework for an Xcode project
feature/settings    a promoted vertical slice — the worked example
design/             theme + components, each with a preview and a showcase
domain/             models, pure logic, repository contracts
data/               repository impls, sources, mappers, HTTP
core/common         combines/tuples, logging, key-value storage
core/ui             ViewModel base, StringValue
di/                 aggregation only
archtest/           the architecture rules, as tests
.github/workflows   check.yml — the command block above, on every push and PR
build-logic/        two convention plugins
```

## Read this before adding code

**[docs/architecture.md](docs/architecture.md)** — which tier a class is, where the file goes, the
ViewModel contract, and what `:archtest` will reject. It is short.

**[docs/upgrade-notes.md](docs/upgrade-notes.md)** — the version-sensitive build facts, each of which
cost a failed build to establish. Read it before bumping Kotlin, AGP, Gradle or Compose.

## What was deliberately left out

Add when you need it, not before:

| Left out | Add when |
|---|---|
| Offline cache | a cold start showing stale data beats showing a spinner |
| A mocking library | never, if you can help it — every KMP one is a KSP processor and would gate each Kotlin bump. Fakes live in `app/src/commonTest/.../fake/` |
| ktlint / detekt | same reason — both parse with Kotlin compiler internals. `.editorconfig` plus the IDE covers formatting |
| Paparazzi / screenshot tests | the design system stabilises and regressions start costing you |
| Crash reporting, analytics | you have users |
| A Coordinator tier | you have a genuinely app-scoped, non-transient orchestrator |
| Per-tab back stacks | a tab gains depth worth preserving (see `Navigator`) |
| Release signing, R8 rules | you are actually shipping — nothing here pretends to be release-ready |
| SQLite on web | the browser driver stops needing an sql.js worker asset and a webpack rule |
