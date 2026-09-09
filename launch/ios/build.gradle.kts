plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "AppFramework"
            isStatic = true
            binaryOption("bundleId", "com.template.app")
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        iosMain.dependencies {
            implementation(projects.app)
            implementation(projects.di)
            implementation(libs.bundles.compose.core)
            implementation(libs.koin.core)
        }
    }
}
