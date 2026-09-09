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
| JVM toolchain | 21, provisioned by the foojay resolver |

## Build setup

**Convention plugins are an included build, not `buildSrc`.** `buildSrc` puts its dependencies on
the root project's classpath, which makes the root build's `alias(...) apply false` fail with
*"plugin is already on the classpath with an unknown version"*. `build-logic/` is registered inside
`pluginManagement` in `settings.gradle.kts` and re-imports `../gradle/libs.versions.toml`, so plugin
versions still have one source of truth.

**AGP 9 has built-in Kotlin support.** Applying `org.jetbrains.kotlin.android` is now a hard error.
`:launch:android` therefore applies only `com.android.application`. (Some older projects opt out
with `android.builtInKotlin=false` in `gradle.properties` — that is the deprecated path; don't.)

**`val desktopMain by getting` is deprecated** in Gradle 9.6+. Use
`getByName("desktopMain").dependencies { }`.

**`material3` and `material3-window-size-class` must sit on the same 1.12 alpha.** window-size-class
carries a constraint that drags material3 up with it, and a material3 built against foundation 1.11
throws `AbstractMethodError` on foundation 1.12 the first time an `OutlinedTextField` composes.

**`:app` disables `checkComposeUiTestConfigurationForWasmJs`.** The Compose plugin registers that
check for any compose + wasmJs module and fails on the missing `binaries.executable()`, even though
this module deliberately produces no artifact (the executable is `:launch:web`). See CMP-4906.

**`gradle/gradle-daemon-jvm.properties` is gitignored.** Gradle generates it from whatever JDK the
generating machine had, with per-platform foojay download ids. The wrapper plus `jvmToolchain(21)`
covers it.

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
