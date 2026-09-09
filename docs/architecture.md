# Architecture

Where code belongs and what to call it. Terse by design.

The goal is that the *structural* part of building a feature becomes paint-by-numbers, so the
thinking goes into the problem instead of into "where does this go, can this call that". If the
scaffolding is boring and dependable, it gets out of the way.

One principle underneath all of it: **separate the code that decides from the code that acts.**
Deciding code — given these facts, what is the answer? — never touches a network, a database, a
clock or a platform API. It stays pure and trivially testable. Everything else gathers facts and
carries out decisions.

## The stack

```
Host → ViewModel → UseCase → { logic, Repository } → Source → Client
```

Arrows are "may depend on". The graph never loops. `logic` is pure and sits to the side.

## Which tier is this?

1. **Is it pure?** Deterministic, no side effects, inputs themselves pure → **`logic`**. Done.
2. **Is its whole job reaching stored data?** → **Repository** (interface in `:domain`, impl in
   `:data`).
3. **Is its whole job reaching a platform capability or fact?** (permissions, locale, clock, device
   info) → **Provider** (interface in `:domain`, impl in `:data`).
4. **Otherwise it is orchestration** → **UseCase**.

That is the whole taxonomy. It is deliberately smaller than the classic clean-architecture set:

- **No Coordinator tier.** Add one when you genuinely have a non-transient, app-scoped orchestrator
  (a sync loop, a connection session) — something with a lifecycle a Host starts. Until then it is
  a tier with no members.
- **No mandatory interface per UseCase.** Give a UseCase an interface when something will fake it.
  A concrete class you construct in a test is not a design flaw.
- **`…Service` is a banned name.** Every role it used to blur now has a precise one. The only
  surviving "Service" is the Android OS class.

### logic

Pure business logic. Same inputs, same answer, nothing else happens.

- Push clock, randomness and I/O to **parameters** — never inject them.
- May depend only on other pure logic.
- Named for the action (`ScoreDayComfort`, `RankDaysByComfort`). One `operator fun invoke`.
- This is where decisions live. A decision anywhere else is a bug waiting for a rewrite.

Example: `domain/weather/logic/ScoreDayComfort.kt` — and note that
`ScoreDayComfortTest` needs no fake, no dispatcher and no test rule.

### UseCase

A transient action: gather → decide → act → done. Keeps nothing between calls.

- Orchestrates repositories and sequences pure logic. May compose other UseCases downward.
- **No relay-only UseCases.** One that forwards a single repository call adds a name and nothing
  else; the ViewModel takes that call directly (see *simple access* below).
- Named for the action, one `operator fun invoke`.

Example: `domain/weather/logic/RankSavedLocationDays.kt` — two repositories plus pure scoring, which
is precisely the job a ViewModel may not do itself.

### Repository

Owner of stored data. Dumb verbs: `get` / `observe` / `save` / `delete`.

- The **mapping membrane**: DTOs and entities never escape to `:domain`, domain models never enter a
  Source.
- Mechanical fetch/cache/upsert lives here. Reconciling with rules does not — that is `logic`,
  sequenced by a UseCase.
- **Repository → Repository is banned.** Combine in a UseCase.
- Throws on failure. Swallowing an error here leaves a screen showing stale data with no way to know.
- Interface in `:domain/<area>/contract/`, impl in `:data/<area>/contract/` — the impl **mirrors the
  interface's package** and is `internal`.

### Local storage

Two stores, one interface, because the platforms genuinely differ:

- `SqlSavedLocationStore` (`data/src/sqliteMain/`) on Android, iOS and desktop.
- `JsonSavedLocationStore` (`commonMain`) on wasmJs, which has no SQLite driver worth shipping —
  the browser one needs an sql.js worker asset and a webpack rule.

The binding is `platformDataModule`, an `expect val Module` with one `actual` per target. That is
the pattern to copy when something is per-platform but not a one-line `expect fun`: the drivers
share no constructor, so a Koin module is the narrowest thing that can differ.

Preferences stay on `KeyValueStore` (multiplatform-settings): `observe`/`get`/`put` for
`String`, `Int` and `Boolean`. A handful of scalars is not a database, and the settings feature
owning its own storage is what keeps it liftable. Add a type to the interface and to
`SettingsKeyValueStore` together — every write has to go through the same revision counter, or
the observers miss it.

**Schema changes.** `SavedLocation.sq` holds the current schema and the queries;
`migrations/<version>.sqm` holds the route an older database takes to reach it. Bump `version` in
`data/build.gradle.kts`, add the next `.sqm`, and never edit an old one — it is the only path an
installed database has. `SavedLocationMigrationTest` builds a v1 database by hand and asserts the
rows survive, which is the only kind of test that catches a migration that drops data.

The SQL a migration may use is set by the *oldest device that will run it*, not by the dev machine:
`minSdk = 26` means Android 8's SQLite 3.18, so no `UPSERT` (3.24) and no `RENAME COLUMN` (3.25).
`migrations/1.sqm` renames a column the long way for exactly that reason.

### Images

`AppImage` in `:design` wraps Coil, with the loading and failure states already decided.
`installAppImageLoader()` in `:app` builds the singleton `ImageLoader` around **the app's own Ktor
client**, so there is one HTTP stack in the process — and because Coil has no network fetcher at
all on iOS or wasmJs without it.

### Source and Client

`:data`'s boundary workers. Deeper gets dumber.

- A Source exists only if it adds something over the thing below it (query construction, error
  handling, combining calls). No relay-only classes.
- Named `<Area><Origin>Source` (`OpenMeteoForecastSource`, `SavedLocationStore`).
- **Source → Repository is banned.**

### Mapper

Pure translation between layer types. Not a tier and not a `logic` unit — it makes no decision.
Top-level extension functions (`fun ForecastResponse.toForecast()`), `internal` to `:data`.

Not ceremony: construct the domain model inline when the mapping is trivially 1:1. A `mapper/`
package appears when the translation earns one.

## Placement — where the file goes

Classification (above) is the strict part. Placement is pragmatic: **start flat, split as it grows.**
Getting classification right is what makes placement cheap — a misplaced file is a five-minute move,
a misclassified one is a rewrite.

**Area-first.** The top package segment in `:domain` and `:data` is the **area** (`weather`, `user`,
`billing`), never the tier. A feature's models, logic and contracts live together.

Start every new area from this template:

```
domain/<area>/
  <Area>DomainDI.kt      # this area's Koin module — with the area, not in a central di/
  logic/                 # pure logic + UseCases
  contract/              # Repository + Provider interfaces
  model/                 # domain models

data/<area>/
  <Area>DataDI.kt
  contract/              # impls, mirroring the :domain package. `internal`.
  source/<origin>/       # remote / local / cache — appears as needed
  mapper/                # appears as needed
```

**DI lives with the area.** You edit bindings in the folder you are already editing, and moving an
area moves its wiring with it. `:di` only aggregates.

**Flat until it grows.** Below the threshold, flat is *correct, not lazy*.

- At roughly **10+ files in a folder, extract a tier** into a sub-package. Below that, leave it.
- Pull `logic/pure/` out first: purity is the one tier property neither the name nor a flat folder
  reveals, so a folder is the cheapest way to make it visible.
- `model/` grows into sub-concepts; `contract/` splits into `repository/` + `provider/` only if it
  gets large.

**Prefer isolated peers over nesting.** The test for an area is: *could this stand alone as a
module?* If yes, it is its own top-level area. Nest a sub-area only when it exists *solely* as part
of its parent. A concept used by two areas cannot nest under one — make it a peer, or park it in
`common/<area>/` until it earns promotion. `common/` is a holding pattern, not a home.

**Source sets.** `commonMain` is the default. Drop to `iosMain`/`androidMain`/`desktopMain`/
`wasmJsMain` only for genuinely platform-specific code, and keep `actual`s in the intermediate set
(`iosMain`, not `iosArm64Main`). Check for a KMP library before reaching for `expect`/`actual`;
prefer an interface with injected implementations for anything complex, and `expect`/`actual` only
for narrow access points — `core/common/.../PlatformSettings.kt` is the right size for one.

## The UI lane

`:app` and `:feature:*` are **feature-shaped and coupled on purpose**. The screen, its state and its
ViewModel change together and are thrown away together. Do not try to make this lane "clean" — make
it cohesive per feature.

### The ViewModel contract

Base: `core/ui/.../viewmodel/StateViewModel.kt`. Canonical examples:
`app/.../weather/bestday/BestDayViewModel.kt`, `feature/settings/.../SettingsViewModel.kt`.

- Subclass `StateViewModel<S>`, or `StateEventViewModel<S, E>` for genuine one-shot effects.
  Implement `WithActions<A>` with a sealed `Action`.
- Override `initialState()`. Build `stateFlow` with `viewModelState { data, state }`. Merge inputs
  with `combines(...)`.
- **The exposed `stateFlow` is derived, never assigned, and never imperatively updated.** Local UI
  state (a query, a selected tab, an expanded row) is a **single** `private MutableStateFlow`
  holding a small record, fed into `data`.
  Not one flow per field: `combines` runs out of arity, and one user action touching three flows
  emits three times, so the screen recomposes against states that never logically existed.
- Reach for the `parameters` overload when several flows in `data` depend on the same upstream
  value — it resolves that value first and re-subscribes through `flatMapLatest`, so one change
  produces one emission. `BestDayViewModel` is the example.
- Navigation goes through the injected `NavControls`. Most screens then need no events at all.
- **Simple access is the one exception to the tier rules:** a ViewModel may read a Repository or
  Provider directly when the call is a plain accessor used as-is in state. The exception ends the
  moment the access combines, decides, fills or writes — that is orchestration, so it is a UseCase.

### Screens

- `Screen(state, onAction)`. No ViewModel parameter, no Koin, no coroutines — so it previews and
  screenshot-tests by constructing a `State`.
- Composables render state. No business logic, no unit conversion, no word choice: all of those are
  decisions made upstream where a test can see them.
- `State` and `Action` share one `…Models.kt` file. They change together, always.
- Every nav entry is the same four lines (`app/.../navigation/entries/`). That uniformity is the
  payoff of the contract.

### Strings

ViewModels put `StringValue` in state, not `String`. A state assertion then compares a resource,
not an English sentence a translator will change next week. Resolve at the leaf, via `AppText`.

Three locales are wired: `values/` (en), `values-sv/`, `values-es/`, in `:app`, `:design` and
`:feature:settings`. **A new key goes into all three files in the same commit** — Compose
Resources falls back to the default silently, so a missing translation looks like a working app.

Where an enum needs a word, map it in a `format/` function (`WeatherCondition.label()`,
`ActivityProfile.label()`) or put the label in state (`SettingsModels.Option`). Never
`StringValue.Raw(someEnum.name)`: an enum name is an identifier and translating it is not a
composable's job.

`StringValue.Raw` is for text that is already final — a place name from the API, a number the
`format/` layer has already rendered. Not for sentences.

### The design system

`:design` is the only place a colour, a type scale, a corner radius or an icon size is decided.
`AppTheme.colors` / `.typography` / `.spacing` / `.sizing` / `.shapes` are how you read them.

Every component carries two things:

- `@Preview`-annotated functions wrapping `AppPreview { }`, so it renders in the IDE. Without the
  wrapper there is no `AppTheme` in scope and the preview throws instead of drawing.
- a public `<Component>Showcase()` holding the demo — every variant and every state a caller can
  get wrong. The previews call it, and so does `app/catalog/AppCatalog.kt`, so the demo exists
  once rather than drifting in two places.

`AppCatalog` (`./gradlew :launch:desktop:run -PappCatalog`) is the whole design system on one
screen in both palettes. It lives in `:app`, not `:design`, so it can also show app-level
components, and it is never a nav destination, so R8 drops it from a release build.

`:archtest` fails a component with no preview and no showcase, a hex literal outside
`design/theme/`, a bare `Text` outside `:design`, and a literal `dp` in a `…Screen.kt`.

## Logging

`Log` in `:core:common`, never `println`. The message is a lambda, so nothing inside it runs when
the level is filtered — including the stack walk that finds the call site.

`callSite()` is `expect`/`actual` because its cost is. The JVM walks `Throwable().stackTrace` and
returns `(BestDayViewModel.kt:88)`, which IntelliJ and most terminals turn into a link. iOS and wasm
return `null` — symbolicating is expensive on Native, and wasm frames name compiled output — so
**pass an explicit tag, `Log.w(TAG) { … }`, in anything you expect to debug on those targets.**

`Log.minimumLevel` is the one filter, set at startup; `:launch:android` reads it off the manifest's
debuggable flag, and `HttpClientFactory` defaults its request logging to `Log.isDebug`. `Log.onLog`
is the hook for a second destination — crash-reporter breadcrumbs, an in-app viewer, a recorder.

## Errors

**Throw** from `:domain` and `:data`. **Catch in the ViewModel** and map to state. No `Result` or
`Either` wrappers in new code. Never catch `CancellationException` — always rethrow.

The ViewModel is the first layer that can turn a failure into something a person can read, which is
why it is the one that catches.

## Modules

| Module              | Role                                                                        |
|---------------------|-----------------------------------------------------------------------------|
| `:app`              | Screens, ViewModels, navigation. Compiles everywhere, runs nowhere.         |
| `:launch:<platform>`| Entry point, packaging and platform chrome. No UI of its own.               |
| `:feature:<name>`   | A promoted vertical slice — see below.                                      |
| `:design`           | Theme and components. The only design system.                               |
| `:domain`           | Pure business layer. No Compose, no Android, no `:data`.                    |
| `:data`             | Repository impls, Sources, Clients, mappers.                                |
| `:core:common`      | Flows/`combines`, logging, key-value storage. No Compose.                   |
| `:core:ui`          | ViewModel base, `StringValue`, state collection.                            |
| `:di`               | Aggregation only, so `:app` never depends on `:data`.                       |
| `:archtest`         | Source-scanning enforcement of this document.                               |
| `build-logic/`      | Two convention plugins, as an included build. Compiled at Java 17 — see upgrade-notes. |

Dependencies: `app → domain, design, core, di, feature:*`; `data → domain`; `domain → core:common`.
The UI lane never touches `:data`.

## Promoting a feature to a module

The default home for a feature is **packages inside the layer modules** — `app/weather/…`,
`domain/weather/…`, `data/weather/…`. That is already feature-oriented, and promoting later is a
folder move.

`:feature:settings` exists as the worked example of the other shape: it owns its screen, ViewModel,
models and storage, depends on no other feature and on neither `:domain` nor `:data`, and exports
exactly three types (`UnitSystem`, `ThemeMode`, `PreferencesRepository`) via `api`.

Promote when a feature has **all** of:

- an owner who ships it independently, or a build time worth cutting;
- a public surface small enough to state in one sentence;
- no need to reach into another feature.

Do not promote by default. In KMP every module multiplies across five targets at configuration
time, and cross-feature sharing turns into an API-export negotiation. `:feature:settings` also shows
the cost: it cannot know the app's navigator, so the host passes `onBack` in
(`app/.../navigation/entries/SettingsEntries.kt`).

## Enforcement

`./gradlew :archtest:test` reads the repo's sources as text and fails on:

- `:domain` importing `:data`, the UI lane, Compose or Android
- the UI lane importing `:data`; a feature importing another feature
- DTOs or Sources escaping `:data`
- a type named `…Service`; a non-`internal` `…RepositoryImpl`
- domain `logic/` reading a clock or randomness
- a ViewModel that does not use `viewModelState`, or that exposes mutable state
- a `Screen` taking a ViewModel, or importing a repository or use case
- a central `di/` package in `:domain` or `:data`
- a hex colour outside `design/theme/`, a bare `Text` outside `:design`, a literal `dp` in a screen
- a design component with no `@Preview` or no `…Showcase()`
- a string key present in `values/` but missing from `values-sv/` or `values-es/`, or vice versa

Text is a coarse tool. It is also a rule you can read in ten lines, which is a rule people keep.
Add a test when you find yourself explaining a convention twice.

The task declares the repo's `*.kt` and `strings.xml` as its inputs. Without that Gradle calls
it UP-TO-DATE after any change outside `:archtest`, and a violation introduced in `:app` passes
locally *and* on a CI runner with a warm cache. If you add a rule that reads a new kind of file,
add it to that `inputs.files` tree too.

## Cross-cutting

- **One coding unit per file, named after it.** Git tracks files: splitting a two-type file later
  creates a *new* file and loses the moved type's history. Exempt: Compose files and `…Models.kt`.
- **Collaborators are injected, never constructed** inside an orchestrator.
- **No side effects in constructors** — especially no coroutine launches in `init`.
- **Impls are `internal`**, to stay unreachable and off the iOS export surface.
- **Decisions belong to `:domain`.** `:data` never decides anything non-obvious.
- **`Log`, never `println`.** One filter, one format, one hook for a crash reporter.
