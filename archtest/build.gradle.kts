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

    /**
     * The files these tests read are not otherwise inputs of this task, so Gradle would call it
     * UP-TO-DATE after any change outside `:archtest` — a violation introduced in `:app` would pass
     * locally and pass again on a CI runner with a warm cache. Declaring the tree it actually reads
     * is what makes the enforcement real, and keeps it cacheable.
     */
    inputs.files(
        rootProject.fileTree(rootProject.layout.projectDirectory) {
            include("**/*.kt", "**/composeResources/**/strings.xml")
            exclude("**/build/**", "**/.gradle/**", "**/.git/**", "**/.kotlin/**", "build-logic/**")
        },
    )
        .withPropertyName("repoSources")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    testLogging { events("passed", "failed") }
}
