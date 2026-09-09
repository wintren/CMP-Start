plugins {
    id("kmp-compose-module")
    alias(libs.plugins.kotlinSerialization)
}

/**
 * The UI lane: screens, ViewModels, navigation. Compiles for every target but produces no runnable
 * artifact — each `:launch:<platform>` module depends on this and supplies the entry point.
 */
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.di)
            implementation(projects.design)
            implementation(projects.core.ui)
            implementation(projects.feature.settings)

            implementation(libs.bundles.compose.core)
            implementation(libs.compose.material.icons)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation3)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.ktor.client.core)

            implementation(libs.bundles.kotlinx)
        }
        androidMain.dependencies { implementation(libs.androidx.activity.compose) }

        // JVM-only: Koin's graph verification needs reflection.
        getByName("desktopTest").dependencies { implementation(libs.koin.test) }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.template.app.resources"
}

// `:app` has Compose UI but no wasm entry point (that lives in `:launch:web`). The Compose plugin
// registers this check for any compose + wasmJs module and fails on the missing
// `binaries.executable()`, even with no Compose UI tests here to bundle. See CMP-4906.
tasks.matching { it.name == "checkComposeUiTestConfigurationForWasmJs" }.configureEach { enabled = false }
