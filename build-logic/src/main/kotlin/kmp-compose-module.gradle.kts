/**
 * A [kmp-module] that also renders UI. Adds the Compose plugins and turns on Android resources so
 * Compose Resources works in the Android variant.
 */
plugins {
    id("kmp-module")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    android {
        androidResources { enable = true }
    }
}

/*
 * None of these modules produce a wasm executable — `:launch:web` does — but the Compose plugin
 * registers this check for every compose + wasmJs module that has test sources and fails it on the
 * missing `binaries.executable()`. See CMP-4906 and docs/upgrade-notes.md.
 */
tasks.matching { it.name == "checkComposeUiTestConfigurationForWasmJs" }.configureEach {
    enabled = false
}
