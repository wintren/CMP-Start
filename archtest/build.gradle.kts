plugins {
    alias(libs.plugins.kotlinJvm)
}

/**
 * Enforces docs/architecture.md by reading source files as text. JVM-only and dependency-free on
 * purpose: it must keep working through every Kotlin, AGP and KMP upgrade, and it needs to see
 * `commonMain` of modules it must not depend on.
 */
kotlin { jvmToolchain(libs.versions.jvm.get().toInt()) }

dependencies {
    testImplementation(libs.kotlin.test)
}

tasks.test {
    // The rules are about the whole repo, not this module.
    systemProperty("repoRoot", rootDir.absolutePath)
    testLogging { events("passed", "failed") }
}
