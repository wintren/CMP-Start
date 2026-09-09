plugins {
    id("kmp-module")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)
            implementation(projects.core.common)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.json)

            implementation(libs.bundles.kotlinx)
            implementation(libs.koin.core)
        }
        // One Ktor engine per target; `HttpClient { }` resolves whichever is on the classpath.
        androidMain.dependencies { implementation(libs.ktor.client.okhttp) }
        iosMain.dependencies { implementation(libs.ktor.client.darwin) }
        getByName("desktopMain").dependencies { implementation(libs.ktor.client.cio) }
        wasmJsMain.dependencies { implementation(libs.ktor.client.js) }

        commonTest.dependencies { implementation(libs.ktor.client.mock) }
    }
}
