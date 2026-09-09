# Upgrade notes

Version-sensitive things that were *empirically* established when this project was set up, not read
from docs. Each one cost a failed build. Read this before bumping Kotlin, AGP, Gradle or CMP.

Verified-working combination (2026-09-09):

| | |
|---|---|
| Gradle | 9.7.1 |
| Kotlin | 2.4.20 |
| AGP | 9.4.0 |
| Compose Multiplatform | 1.12.0 |
| material3 / window-size-class | 1.12.0-alpha03 (must match each other) |
| lifecycle (JetBrains) | 2.11.0 |
| navigation3-ui | 1.1.1 |
| Koin | 4.2.2 |
| Ktor | 3.5.2 |
| coroutines / serialization | 1.11.0 / 1.11.0 |
| kotlinx-datetime | 0.8.0 |
| multiplatform-settings | 1.3.0 |
| SQLDelight | 2.3.2 |
| Coil | 3.5.0 |
| JVM toolchain | 21, provisioned by the foojay resolver |

## Build setup

**Convention plugins are an included build, not `buildSrc`.** `buildSrc` puts its dependencies on
the root project's classpath, which makes the root build's `alias(...) apply false` fail with
*"plugin is already on the classpath with an unknown version"*. `build-logic/` is registered inside
`pluginManagement` in `settings.gradle.kts` and re-imports `../gradle/libs.versions.toml`, so plugin
versions still have one source of truth.

**`build-logic` compiles at Java 17, on purpose.** `kotlin-dsl` otherwise compiles the convention
plugins at whatever JVM started Gradle. Run once from a JDK 25 IDE and the build cache holds
class-file 69; the next run from a JDK 17 shell dies with
`UnsupportedClassVersionError: KmpModulePlugin has been compiled by a more recent version of the
Java Runtime`. 17 is the floor for a Gradle 9 daemon, so bytecode at that level loads under any of
them. Module code still targets 21 via `jvmToolchain`. This is also why
`gradle/gradle-daemon-jvm.properties` is not needed.

**AGP 9 has built-in Kotlin support.** Applying `org.jetbrains.kotlin.android` is now a hard error.
`:launch:android` therefore applies only `com.android.application`. (Some older projects opt out
with `android.builtInKotlin=false` in `gradle.properties` — that is the deprecated path; don't.)

**`val desktopMain by getting` is deprecated** in Gradle 9.6+. Use
`getByName("desktopMain").dependencies { }`.

**`material3-window-size-class` is in the catalog but no longer on any module's classpath** —
`AppTheme.windowSize` measures `LocalWindowInfo` instead. Nothing resolves it today, so the
constraint below is dormant; if you never bring it back, `compose-material3` can go to the stable
1.12.0 and drop the alpha. Verify a build on every target if you try that.

**`material3` and `material3-window-size-class` must sit on the same 1.12 alpha.** window-size-class
carries a constraint that drags material3 up with it, and a material3 built against foundation 1.11
throws `AbstractMethodError` on foundation 1.12 the first time an `OutlinedTextField` composes.

**`:app` disables `checkComposeUiTestConfigurationForWasmJs`.** The Compose plugin registers that
check for any compose + wasmJs module and fails on the missing `binaries.executable()`, even though
this module deliberately produces no artifact (the executable is `:launch:web`). See CMP-4906.

**Warnings are errors**, set once in the root `build.gradle.kts` for every subproject. A Kotlin,
AGP or CMP bump deprecates things in batches, so build with `-PlenientWarnings` while you migrate
and drop the flag before you commit — that is the difference between reading the warnings once and
never reading them.

**`org.gradle.parallel` and `configureondemand` are off** because of the wasmJs target (KT-52074).
Turn them on if you drop web.

## Library specifics

**`multiplatform-settings-no-arg` publishes no wasmJs variant** (only `js`); the base library does.
Hence the single `expect`/`actual` in `core/common/.../storage/PlatformSettings.kt`: no-arg on
Android/iOS/desktop, `StorageSettings()` on wasmJs. Check this if you bump the library hoping to
delete that file.

**`org.koin.core.context.GlobalContext` is not on the native source set** — using it fails the iOS
link. `startAppKoin()` in `app/.../di/` uses `org.koin.mp.KoinPlatform` instead, and every
`:launch:<platform>` module calls that one function.

**Koin's `Module.verify()` verifies each module in isolation**, so a binding provided by a *different*
module reports as missing. `AppModulesTest` constructs the real graph and resolves each type
instead — which is also a stronger check, because it runs each ViewModel's `init` and its
`viewModelState` builder.

**kotlinx-datetime 0.8** renamed `LocalDate.dayOfMonth` to `LocalDate.day`. `kotlin.time.Clock` with
`kotlinx.datetime.todayIn` is the current pairing.

**`Icons.Default.HelpOutline` is deprecated** in favour of `Icons.AutoMirrored.Filled.HelpOutline`.
Build with warnings visible; the material-icons artifact moves things every release.

**SQLDelight's default dialect is SQLite 3.18, and that is the right one here.** `minSdk = 26`
means Android 8, whose bundled SQLite is 3.18 — so `UPSERT` (3.24) and `ALTER TABLE … RENAME
COLUMN` (3.25) parse on the dev machine's dialect but fail on a real phone. `migrations/1.sqm`
renames a column the long way (rename table, recreate, copy, drop) for that reason. Raising the
dialect means raising `minSdk` past 30 first.

**SQLDelight generates into `commonMain`, so the runtime resolves on every target — including
wasmJs, which has no driver.** `sqldelight-runtime` and `coroutines-extensions` do publish wasmJs
artifacts; only the drivers do not. Hence the `sqliteMain` intermediate source set for
Android/iOS/desktop and `platformDataModule` binding `JsonSavedLocationStore` on web. Shipping
SQLite to a browser needs the `web-worker-driver` plus an sql.js worker asset and a webpack rule to
serve it — check whether that has become one line before assuming this split is still necessary.

**`JdbcSqliteDriver` does not manage schema versions.** The Android and native drivers take
`AppDatabase.Schema` and run `create`/`migrate` themselves; the JDBC one will happily open a v1
file against v2 code and fail on the first query. `PlatformDataModule.desktop.kt` does the
`PRAGMA user_version` dance by hand. Delete that only when the driver grows the feature.

**Coil is given the app's Ktor client.** `installAppImageLoader()` passes the injected `HttpClient`
into `KtorNetworkFetcherFactory`, so `:design` needs no engine of its own and the process has one
HTTP stack. Coil has no network fetcher at all on iOS or wasmJs without that registration — images
fail silently rather than throwing, so a blank image is the symptom to look for.

**`w: Skiko dependencies' versions are incompatible.` on the wasm build is Coil, and it is
harmless as it stands.** Coil 3.5.0 requests skiko 0.144.6; CMP 1.12.0 requests 0.150.1 and Gradle
resolves everything to 0.150.1, so the *classpath* has one version — the plugin warns on the
*requested* graph. Check `./gradlew :launch:web:dependencies --configuration wasmJsRuntimeClasspath
| grep skiko` after bumping either: the day the resolved column shows two versions, web rendering
breaks at runtime rather than at build time.

**Compose `@Preview` works in `commonMain`** through
`org.jetbrains.compose.ui:ui-tooling-preview`, imported as `androidx.compose.ui.tooling.preview.Preview`.
A preview must be wrapped in `AppPreview { }`: `AppTheme.colors` reads a `staticCompositionLocalOf`
that throws when unset, so an unwrapped preview fails to render rather than rendering unstyled.

## Testing a `StateViewModel`

Two traps, both of which produce a test that reads `initialState()` forever and looks like a
ViewModel bug:

1. `stateFlowMode` defaults to `WhileSubscribed`, so **state is only computed while something
   collects it**. Collect on `backgroundScope` for the length of the test.
2. `runTest { }` defaults to `StandardTestDispatcher`, which merely *queues* that collector. Use
   `runTest(UnconfinedTestDispatcher())` — and `Dispatchers.setMain(UnconfinedTestDispatcher())`,
   because `viewModelScope` dispatches on Main.

`app/src/commonTest/.../BestDayViewModelTest.kt` is the worked example.

## Sanity check after any bump

```bash
./gradlew :archtest:test \
          :domain:desktopTest :data:desktopTest :app:desktopTest \
          :launch:android:assembleDebug \
          :launch:desktop:compileKotlinDesktop \
          :launch:web:wasmJsBrowserDistribution \
          :launch:ios:linkDebugFrameworkIosSimulatorArm64
```

Compilation alone does not prove the app works: the Koin graph resolves at runtime. `:app:desktopTest`
is what catches a missing binding.
