plugins {
    id("kmp-module")
}

/**
 * Aggregation only, so `:app` can start the Koin graph without depending on `:data`.
 * No feature code ever lands here.
 */
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.domain)
            implementation(projects.data)
            implementation(libs.koin.core)
        }
    }
}
