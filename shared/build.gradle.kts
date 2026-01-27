import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.sampletest.corePlugin)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sampletest.koinPlugin)
}

kotlin {

    androidLibrary {

        namespace = "com.sampletest.shared"

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

//            implementation(projects.core.common)


            implementation(libs.bundles.compose)
            implementation(libs.bundles.material3)
            implementation(libs.bundles.navigation3)
            implementation(libs.jetbrains.lifecycleRuntime)
            implementation(libs.jetbrains.lifecycleViewmodel)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }


}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.uiTooling)
}