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
   catch `CancellationException`.
6. **DI lives with the area** (`<area>/<Area>DomainDI.kt`), never in a central `di/`. Constructor
   injection only. `single<Interface> { new(::Impl) }`.
7. **`…Service` is a banned name.** Use Repository / UseCase / Source / Client / logic.
8. **One top-level type per file, named after it.** Exempt: Compose files and `…Models.kt`.
9. **Design system only.** `com.template.design.*` — `AppText`, `AppButton`, `AppTheme.colors`.
   No `Color(0x…)` outside `design/theme/`, no bare `Text` outside `:design`, and a `…Screen.kt`
   takes every measurement from `AppTheme.spacing` / `AppTheme.sizing`. A **component** may use a
   literal `dp` — a chip's 2dp inset is local to it. Icon sizes are always a `sizing` role.
10. **Every design component has a `@Preview` and a `…Showcase()`.** The preview wraps the
    showcase in `AppPreview { }`; `app/catalog/AppCatalog.kt` renders the same showcase, so the
    demo is written once. `:archtest` fails a component that has neither.
11. **No user-visible `String` in code.** Screens and ViewModels carry `StringValue`; the words
    live in `composeResources/values*/strings.xml`. `StringValue.Raw` is for text that is already
    final — a place name, a formatted number — never for a sentence.
12. **No KDoc that restates the signature.** A comment earns its line by explaining *why*, naming a
    constraint the type cannot express, or recording a decision a reader would otherwise undo.
13. **Use `combines()`, never `combine()`.** The array overload erases every type past the first.
14. **No new dependency** without saying what it replaces. Especially no mocking library and no
    compiler plugins — they gate Kotlin upgrades.
15. **Fix what you touch.** No drive-by refactors.

## Verify before you claim done

```bash
./gradlew :archtest:test                                   # the rules
./gradlew :domain:desktopTest :data:desktopTest :app:desktopTest
./gradlew :launch:desktop:compileKotlinDesktop :launch:android:assembleDebug
```

`./gradlew :launch:desktop:run -PappCatalog` opens the design-system catalog — every token and
component in both palettes. Use it after touching `:design`.

`:app:desktopTest` includes `AppModulesTest`, which constructs the whole Koin graph — a missing
binding is a runtime crash otherwise, and on iOS or wasm it surfaces late.

Non-trivial logic leaves one runnable check behind. Pure logic gets a plain unit test; a data path
gets `MockEngine`; a ViewModel gets fakes from `app/src/commonTest/.../fake/`.

## Translations

`en` (`values/`), `sv` (`values-sv/`), `es` (`values-es/`) in `:app`, `:design` and
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
