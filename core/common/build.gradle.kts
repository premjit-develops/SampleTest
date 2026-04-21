import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.sampletest.corePlugin)
    alias(libs.plugins.sampletest.koinPlugin)
    alias(libs.plugins.sampletest.supabasePlugin)
}

kotlin {

    android {

        namespace = "com.sampletest.core.common"

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

            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.jetbrains.composeRuntime)

        }

        commonTest.dependencies {

            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutinesTest)
        }


    }

}