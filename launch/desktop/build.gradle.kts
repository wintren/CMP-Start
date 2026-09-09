import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
    jvm("desktop")

    sourceSets {
        getByName("desktopMain").dependencies {
            implementation(projects.app)
            implementation(projects.di)
            implementation(projects.design)

            implementation(compose.desktop.currentOs)
            implementation(libs.bundles.compose.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

// `-PappCatalog` swaps the entry point for the design-system catalog. Same module, same
// dependencies — a separate `:launch:catalog` module would only duplicate this file.
val runsCatalog = providers.gradleProperty("appCatalog").isPresent

compose.desktop {
    application {
        mainClass = when {
            runsCatalog -> "com.template.launch.desktop.CatalogMainKt"
            else -> "com.template.launch.desktop.MainKt"
        }

        // `run` otherwise launches on the machine's default JVM, which fails with
        // UnsupportedClassVersionError whenever that is older than the toolchain we compile against.
        javaHome = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.get().toInt()))
        }.get().metadata.installationPath.asFile.absolutePath

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CMP Start"
            packageVersion = "1.0.0"
            vendor = "Template"
        }
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get())) }
}
