import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}


group = "com.sampletest.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}


kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}


dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlinMultiplatform.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}


gradlePlugin {

    plugins {


        register("corePlugin") {
            id = libs.plugins.sampletest.corePlugin.get().pluginId
            implementationClass = "CoreConventionPlugin"
        }

        register("featurePlugin") {
            id = libs.plugins.sampletest.featurePlugin.get().pluginId
            implementationClass = "FeatureConventionPlugin"
        }

        register("koinPlugin") {
            id = libs.plugins.sampletest.koinPlugin.get().pluginId
            implementationClass = "KoinConventionPlugin"
        }

        register("supabasePlugin") {
            id = libs.plugins.sampletest.supabasePlugin.get().pluginId
            implementationClass = "SupabaseConventionPlugin"
        }

    }
}