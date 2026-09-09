plugins {
    id("kmp-module")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.template.data.database")
            // Bump on every schema change and add the matching `<previous>.sqm` under
            // src/commonMain/sqldelight/migrations/. See migrations/1.sqm.
            version = 2
        }
    }
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

            // The generated database lands in commonMain, so the runtime has to resolve on every
            // target — including wasmJs, which has no driver to go with it.
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.bundles.kotlinx)
            implementation(libs.koin.core)
        }

        // The three targets with a real SQLite behind them. wasmJs is not here: shipping SQLite to
        // the browser means an sql.js worker asset and a webpack rule, which a starter should not
        // carry. Its store is backed by the key-value store instead.
        val sqliteMain by creating {
            dependsOn(commonMain.get())
        }
        androidMain {
            dependsOn(sqliteMain)
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.sqldelight.driver.android)
            }
        }
        iosMain {
            dependsOn(sqliteMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.driver.native)
            }
        }
        getByName("desktopMain") {
            dependsOn(sqliteMain)
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.sqldelight.driver.sqlite)
            }
        }
        wasmJsMain.dependencies { implementation(libs.ktor.client.js) }

        commonTest.dependencies { implementation(libs.ktor.client.mock) }
    }
}
