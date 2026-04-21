import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.sampletest.corePlugin)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    android {

        namespace = "com.sampletest.core.design"

        compileSdk = libs.versions.compileSdk.get().toInt()

        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.bundles.material3)
        }

    }


}