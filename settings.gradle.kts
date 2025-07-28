pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "decimal-formatter"
include(":decimal-formatter-compose")
include(":decimal-formatter-core")
include(":shared")
include(":app")
