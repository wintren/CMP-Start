import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    // Declared here so each subproject's classloader doesn't load them repeatedly.
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidKmpLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
}

/**
 * Warnings are errors everywhere, including the `:launch:*` modules that use no convention plugin —
 * a Kotlin or CMP bump deprecates things in batches, and a warning nobody is forced to read is a
 * migration nobody does. `-PlenientWarnings` is the escape hatch while you are mid-upgrade.
 */
val warningsAreErrors = !providers.gradleProperty("lenientWarnings").isPresent

subprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions { allWarningsAsErrors.set(warningsAreErrors) }
    }
}
