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
