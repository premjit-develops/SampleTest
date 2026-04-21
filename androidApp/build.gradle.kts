import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.koinCompilerPlugin)
}


kotlin {

    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    dependencies {

        implementation(projects.shared)

        implementation(libs.androidx.activityCompose)
        implementation(libs.core.splashscreen)

        implementation(libs.jetbrains.lifecycleRuntime)
        implementation(libs.jetbrains.lifecycleViewmodel)

        implementation(platform(libs.koin.bom))
        implementation(libs.koin.android)

        debugImplementation(libs.jetbrains.uiToolingPreview)

    }

}

android {

    namespace = "com.sampletest"
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt())
    }

    defaultConfig {
        applicationId = "com.sampletest"
        minSdk = libs.versions.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {

    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(projects.baselineprofile)
    debugImplementation(libs.jetbrains.uiTooling)

}