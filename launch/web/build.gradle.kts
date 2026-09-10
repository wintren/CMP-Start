import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("app")
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply { port = 8081 }
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(projects.app)
            implementation(projects.di)
            implementation(libs.bundles.compose.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.browser)
            implementation(libs.kotlinx.coroutines)
        }
    }
}

// No test sources here; the default task otherwise fails with "no tests discovered".
tasks.matching { it.name == "wasmJsBrowserTest" || it.name == "wasmJsNodeTest" }
    .configureEach { enabled = false }
