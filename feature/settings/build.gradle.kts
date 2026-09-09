plugins {
    id("kmp-compose-module")
}

/**
 * A promoted vertical slice: owns its screen, ViewModel, models and storage, and exposes the few
 * types other features read (`UnitSystem`, `ThemeMode`, `PreferencesRepository`) via `api`.
 * It depends on no other feature and on neither `:domain` nor `:data` — that self-containment is
 * the whole reason to make a feature a module. See docs/architecture.md.
 */
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.ui)
            implementation(projects.design)

            implementation(libs.bundles.compose.core)
            implementation(libs.compose.material.icons)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.template.feature.settings.resources"
}
