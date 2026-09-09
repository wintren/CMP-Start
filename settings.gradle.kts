rootProject.name = "CMP_Start"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    // Convention plugins: `kmp-module`, `kmp-compose-module`.
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Provisions the JVM toolchain declared by the modules, so a machine without that exact JDK
// installed still builds. Without it, `jvmToolchain(21)` fails on a JDK-17-only machine.
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app")
include(":di")
include(":design")
include(":domain")
include(":data")

include(":core")
include(":core:common")
include(":core:ui")

// Feature modules: a promoted vertical slice. See docs/architecture.md — "Promoting a feature".
include(":feature")
include(":feature:settings")

// Platform entry points. `:app` deliberately produces no runnable artifact of its own.
include(":launch")
include(":launch:android")
include(":launch:desktop")
include(":launch:web")
include(":launch:ios")

// Source-scanning tests that enforce docs/architecture.md. JVM-only, runs anywhere.
include(":archtest")
