plugins {
    id("kmp-compose-module")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(libs.lifecycle.viewmodel)
            api(libs.lifecycle.runtime.compose)
            implementation(libs.compose.runtime)
            implementation(libs.compose.resources)
            implementation(libs.kotlinx.coroutines)
        }
    }
}
