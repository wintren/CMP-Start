plugins {
    id("kmp-compose-module")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.ui)
            api(libs.bundles.compose.core)
            api(libs.compose.material.icons)
            implementation(libs.compose.ui.tooling.preview)
        }
    }
}
