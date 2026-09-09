plugins {
    `kotlin-dsl`
}

/**
 * Pinned to 17, not to the project's `jvm = 21`.
 *
 * `kotlin-dsl` otherwise compiles these plugins at whatever JVM started Gradle, so a run from a
 * JDK 25 IDE writes class-file 69 into the build cache and the next run from a JDK 17 shell dies
 * with `UnsupportedClassVersionError: KmpModulePlugin ... class file version 69.0`. 17 is the
 * minimum a Gradle 9 daemon can run on, so bytecode at that level loads under any of them.
 *
 * This governs the *convention plugins only*. Module code still compiles and targets 21 through
 * `jvmToolchain` in kmp-module.gradle.kts.
 */
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.compose)
    implementation(libs.gradlePlugin.composeCompiler)
}
