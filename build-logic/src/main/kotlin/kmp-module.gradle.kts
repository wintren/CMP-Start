import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * Every shared module's target set and toolchain. Dependencies stay in the module's own build file
 * — this plugin owns *where the code compiles*, not what it depends on.
 *
 * The Android namespace is derived from the Gradle path (`:core:common` -> `com.template.core.common`),
 * so adding a module needs no namespace bookkeeping. `scripts/rename.sh` rewrites the base package.
 */
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

private val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun version(name: String): String = catalog.findVersion(name).get().requiredVersion

val basePackage = "com.template"
val moduleNamespace = basePackage + project.path.replace(":", ".").replace("-", "")

kotlin {
    jvmToolchain(version("jvm").toInt())

    android {
        namespace = moduleNamespace
        compileSdk = version("android-compileSdk").toInt()
        minSdk = version("android-minSdk").toInt()
        withHostTest { isReturnDefaultValues = true }
    }

    jvm("desktop")

    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonTest.dependencies {
            implementation(catalog.findLibrary("kotlin-test").get())
            implementation(catalog.findLibrary("kotlinx-coroutines-test").get())
        }
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.fromTarget(version("jvm"))) }
}
