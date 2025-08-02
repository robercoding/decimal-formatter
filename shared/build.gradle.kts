plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {

// Target declarations - add or remove as needed below. These define
// which platforms this KMP module supports.
// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "dev.robercoding.decimal.formatter"
        compileSdk = 35
        minSdk = 33

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

// For iOS targets, this is also where you should
// configure native binary output. For more information, see:
// https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

// A step-by-step guide on how to include this library in an XCode
// project can be found here:
// https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "shared"
    val bundleId = "dev.robercoding.decimal.formatter.shared"

    iosX64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", bundleId)
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", bundleId)
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
            binaryOption("bundleId", bundleId)
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":decimal-formatter-compose"))
                implementation(libs.kotlin.stdlib)
                implementation(compose.components.resources)
                implementation(compose.material3)
                implementation(libs.foundation)
                // implementation(libs.androidx.viewmodel.compose)
                implementation(libs.ionspin.kotlin.bignum)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Empty
                implementation(libs.androidx.lifecycle.viewmodel.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependencies {
                // Empty
            }
        }
    }

}