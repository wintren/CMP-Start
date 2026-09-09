import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // AGP 9 has built-in Kotlin support — the `org.jetbrains.kotlin.android` plugin is gone.
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.template.launch.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.template.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        // Deep links arrive as `<applicationId>://<route>`; see the VIEW filter in the manifest.
        manifestPlaceholders["appLinkScheme"] = applicationId as String
    }

    buildTypes {
        debug { applicationIdSuffix = ".debug" }
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions { jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get())) }
}

dependencies {
    implementation(projects.app)
    implementation(projects.di)
    // For the startup log level. `:app` depends on `:core:common` with `implementation`, so it is
    // not on this module's compile classpath transitively.
    implementation(projects.core.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.compose.ui.tooling.preview)
    // Preview renderer. Lives here rather than in :design because the KMP Android library plugin
    // is single-variant and has no debug-only configuration to scope it to.
    debugImplementation(libs.compose.ui.tooling)
}
