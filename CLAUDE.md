# CLAUDE.md

Kotlin Multiplatform / Compose Multiplatform starter. Android, iOS, Desktop (JVM), Web (WasmJS).

> Verify library APIs against `gradle/libs.versions.toml` and the actual code — do not guess from
> training data. This project runs versions newer than most training cutoffs.

Keep this file short. Depth belongs in `docs/architecture.md` (conventions) and
`docs/upgrade-notes.md` (version-sensitive build facts — read before bumping Kotlin, AGP or CMP).

## Before acting

Name the scope: refactor, bugfix, new UI, or new logic. When in doubt, ask.

**Read `docs/architecture.md` before creating any new class.** It answers which tier a thing is,
where the file goes, and what `:archtest` will reject. Pushing back on a design that violates it is
part of the job.

## Hard rules

1. **`stateFlow` is derived, never assigned.** Build it with `viewModelState`. Local UI state is
   **one** `private MutableStateFlow` holding a small record, fed into `data` — not one flow per
   field.
2. **Screens take `state` and `onAction`.** No ViewModel parameter, no Koin, no coroutines, no
   business logic, no unit conversion in a composable.
3. **`:domain` is pure Kotlin.** No Compose, no Android, no `:data`. Pure `logic` takes its clock
   and randomness as parameters.
4. **The UI lane never imports `:data`.** Repository interfaces live in `:domain`, impls in `:data`,
   `internal`, in the package mirroring the interface.
5. **Throw in `:domain`/`:data`, catch in the ViewModel.** No `Result`/`Either` wrappers. Never
   catch `CancellationException`. `fire(onError = …) { }` is the catch — no `runCatching` in a
   ViewModel. Transport failures arrive as `AppException`; `Throwable.asMessage()` turns one into
   words.
6. **DI lives with the area** (`<area>/<Area>DomainDI.kt`), never in a central `di/`. Constructor
   injection only. `single<Interface> { new(::Impl) }`.
7. **`…Service` is a banned name.** Use Repository / UseCase / Source / Client / logic.
8. **One top-level type per file, named after it.** Exempt: Compose files and `…Models.kt`.
9. **Design system only.** `com.template.design.*` — `AppText`, `AppButton`, `AppIcon`,
   `AppTheme.colors`. No `Color(0x…)` outside `design/theme/`, no bare `Text` or `Icon` outside
   `:design`, no `contentDescription` string literal anywhere, and a `…Screen.kt`
   takes every measurement from `AppTheme.spacing` / `AppTheme.sizing`. A **component** may use a
   literal `dp` — a chip's 2dp inset is local to it. Icon sizes are always a `sizing` role.
10. **Every design component has a `@Preview` and a `…Showcase()`.** The preview wraps the
    showcase in `AppPreview { }`; `app/catalog/AppCatalog.kt` renders the same showcase, so the
    demo is written once. `:archtest` fails a component that has neither.
11. **No user-visible `String` in code** — including a `contentDescription`, which a screen reader
    reads out. Screens and ViewModels carry `StringValue`; the words live in
    `composeResources/values*/strings.xml`. `StringValue.Raw` is for text that is already
    final — a place name, a formatted number, a `—` placeholder. `:archtest` fails a key that
    is missing from any locale.
12. **Comments are noise until proven otherwise.** Default to none. Run all four tests before
    writing one, and on every comment in a file you touch — deleting a failing comment is not a
    drive-by refactor:
    - **Restatement.** Every fact already in the name, signature, type or surrounding context →
      delete. `minimumLevel` inside a level filter needs nothing.
    - **Altitude.** Teaching a concept — KMP, Compose, coroutines, this architecture → delete. That
      belongs in `docs/`, written once.
    - **Scope.** A fact about a caller, a call site, or a collaborator's internals → delete. A
      function knows what it does, not who uses it, and not how the repository it calls stores
      anything.
    - **Undo.** Would a senior developer, reading only the code, change it for the worse? That is
      the only thing that earns a comment: an ordering constraint, a non-obvious side effect, a
      workaround, a decision that looks wrong until you know what broke.
    State what the thing *is*, not why it isn't something else. Caveats belong on the `actual` or
    the branch that has them, not on the declaration everyone reads.
    One line. Two if it earns it. Longer *only* for a decision a reader would otherwise undo, and
    then state what breaks and the issue reference — not the reasoning. Type-level KDoc only when
    the type is genuinely complex or overloaded. No usage examples in code, and no `@param`/
    `@return` that repeats the signature.
13. **Use `combines()`, never `combine()`.** The array overload erases every type past the first.
14. **No new dependency** without saying what it replaces. Especially no mocking library and no
    compiler plugins — they gate Kotlin upgrades.
15. **`Log`, never `println`.** `Log.d { "…" }` — the message is a lambda so nothing is built
    when the level is filtered. Pass a `tag` in anything you will debug on iOS or wasm; those
    targets have no call site, because taking one there is expensive. See `docs/architecture.md`.
16. **Fix what you touch.** No drive-by refactors.
17. **Time comes from an injected `Clock`.** `clock.today()`, `thisWeek()` and the rest live in
    `core/common/time/`. `Clock.System` is bound once in `coreCommonModule` and `:archtest` fails
    it anywhere else; tests pass a `FixedClock`.
18. **No environment literal in code.** Base URLs, flags and tokens are keys in
    `config/<env>.properties`, read as `AppConfig.<key>`. Adding a key means adding it to all three
    files. Secrets come from `local.properties` or `APP_*` — never from git.

19. **A new `Destination` is a new route.** Add it to `Routes.kt` in the same edit: one codec
    serves the browser URL, Android deep links and the saved session. Only a host calls
    `Navigator.restore()`.
20. **Layout reads a width, not a device.** `AppTheme.windowSize` for the window,
    `AppWindowSize.of(maxWidth)` inside a `BoxWithConstraints` for a pane. Only `AppTheme` reads
    `LocalWindowInfo`, and `:archtest` enforces it. The back stack never changes with the window:
    `listPaneOf()` decides how the last two entries are *rendered*.

## Verify before you claim done

```bash
./gradlew :archtest:test                                   # the rules
./gradlew :domain:desktopTest :data:desktopTest :app:desktopTest :core:common:desktopTest :design:desktopTest
./gradlew :launch:desktop:compileKotlinDesktop :launch:android:assembleDebug
```

The same `commonTest` sources run on Kotlin/Native and wasm, and both disagree with the JVM often
enough to matter — a comma in a test name does not even compile on Native:

```bash
./gradlew :domain:iosSimulatorArm64Test :core:common:iosSimulatorArm64Test :app:iosSimulatorArm64Test
./gradlew :domain:wasmJsBrowserTest :core:common:wasmJsBrowserTest :app:wasmJsBrowserTest
```

`./gradlew :launch:desktop:run -PappCatalog` opens the design-system catalog — every token and
component in both palettes. Use it after touching `:design`.

`:app:desktopTest` includes `AppModulesTest`, which constructs the whole Koin graph — a missing
binding is a runtime crash otherwise, and on iOS or wasm it surfaces late.

Non-trivial logic leaves one runnable check behind. Pure logic gets a plain unit test; a data path
gets `MockEngine`; a ViewModel gets fakes from `app/src/commonTest/.../fake/`.

## Translations

`en` (`values/`), `sv` (`values-sv/`), `es` (`values-es/`) in `:app`, `:design`, `:core:ui` and
`:feature:settings`.

**Adding a string means adding it to all three files in the same edit — do that without asking.**
A best-effort `sv`/`es` translation that a native speaker corrects later beats a missing key that
falls back to English at runtime and nobody notices. Flag anything genuinely ambiguous (a pun, a
term of art, a string whose grammar depends on a number) rather than guessing quietly.

## Naming

This is a template. `com.template` and `CMP_Start` are placeholders that `scripts/rename.sh`
rewrites, so keep new code inside the base package and do not hardcode either string anywhere the
script cannot reach.

## Always ask

- Architecture decisions — which layer owns a thing, whether a feature earns its own module
- Whether surrounding code is correct or legacy
- Destructive or hard-to-reverse actions
- Adding or bumping a dependency

## Communication

Short, direct, honest. Think before output.

- State results and decisions. One sentence is usually enough.
- Reference files as `path:line`.
- Name blockers explicitly; do not paper over them.
- No sycophantic openers, no summaries of what the diff already shows, no motivational closers.
- Do not add features, refactors or error handling that were not asked for.
- Do not invent APIs. If unsure a thing exists, check or ask.
