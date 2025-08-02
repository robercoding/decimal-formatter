import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        dependencies {
            androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.8.2")
            debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.2")
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":decimal-formatter-core"))


                implementation(compose.components.resources)
                implementation(libs.ionspin.kotlin.bignum)
                implementation(compose.material3)
                implementation(libs.foundation)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
            }
        }
    }
}

android {
    namespace = "dev.robercoding.decimal.formatter.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    pom {
        // artifact id is set automatically to the project name, in this case "decimal-formatter-compose"
        name.set("Decimal Formatter Compose")
        description.set("Jetpack Compose UI components for decimal number input with automatic formatting. Includes OutlinedDecimalTextField, BasicDecimalTextField, and DecimalText.")
    }
    publishToMavenCentral()
    signAllPublications()
}
