plugins {
    id("kmp-module")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.koin.core)
        }
    }
}
