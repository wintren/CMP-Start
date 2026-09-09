rootProject.name = "build-logic"

// Convention plugins live in an *included build*, not buildSrc: buildSrc puts its dependencies on
// the root project's classpath, which breaks `alias(...) apply false` in the root build with
// "plugin is already on the classpath with an unknown version".
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") { from(files("../gradle/libs.versions.toml")) }
    }
}
