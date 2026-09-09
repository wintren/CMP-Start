plugins {
    id("kmp-module")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.multiplatform.settings)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.koin.core)
        }
        // `multiplatform-settings-no-arg` publishes no wasmJs variant (only `js`), so the web
        // target builds its Settings by hand — see platformSettings.wasmJs.kt.
        androidMain.dependencies { implementation(libs.multiplatform.settings.no.arg) }
        iosMain.dependencies { implementation(libs.multiplatform.settings.no.arg) }
        getByName("desktopMain").dependencies { implementation(libs.multiplatform.settings.no.arg) }
    }
}
