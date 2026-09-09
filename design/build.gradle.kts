plugins {
    id("kmp-compose-module")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.template.design.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.ui)
            api(libs.bundles.compose.core)
            api(libs.compose.material.icons)
            api(libs.coil.compose)
            implementation(libs.compose.ui.tooling.preview)
        }
    }
}
