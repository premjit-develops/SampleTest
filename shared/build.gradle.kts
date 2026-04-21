@file:OptIn(ApolloExperimental::class)

import com.apollographql.apollo.annotations.ApolloExperimental
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.sampletest.corePlugin)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sampletest.koinPlugin)
    alias(libs.plugins.sampletest.supabasePlugin)
    alias(libs.plugins.apolloPlugin)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinSerialization)
}


val localProperties: Properties by lazy {
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        } else {
            logger.warn("local.properties not found. Secrets may be missing.")
        }
    }
}

fun localProp(key: String): String =
    localProperties.getProperty(key) ?: error("Missing `$key` in local.properties")


kotlin {

    android {

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

            implementation(projects.core.common)
            implementation(projects.core.design)


            implementation(libs.bundles.compose)
            implementation(libs.bundles.material3)
            implementation(libs.bundles.navigation3)
            implementation(libs.jetbrains.lifecycleRuntime)
            implementation(libs.jetbrains.lifecycleViewmodel)

            implementation(libs.kotlinx.coroutinesCore)

            implementation(libs.apollo.normalizedCache)
            implementation(libs.apollo.normalizedCacheSqlite)
            implementation(libs.apollo.adaptersCore)

        }

        androidMain.dependencies {
            implementation(libs.ktor.clientOkhttp)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    compilerOptions {
        freeCompilerArgs.add(
            "-Xopt-in=kotlin.time.ExperimentalTime",
        )
    }


}


apollo {

    service("service") {

        packageName.set("com.sampletest.shared")

        srcDir("src/commonMain/graphql")


        introspection {
            // Check common/Constants
            endpointUrl.set(localProp("SUPABASE_GRAPH_ENDPOINT"))
            headers.put("apikey", localProp("SUPABASE_API_KEY"))
            schemaFile.set(file("src/commonMain/graphql/schema.graphqls"))
        }

        generateApolloMetadata.set(true)
//        generateKotlinModels.set(true)
//        generateDataBuilders.set(true)


        mapScalarToKotlinString("UUID")
        mapScalar(
            "Datetime",
            "kotlin.time.Instant",
            "com.apollographql.adapter.core.KotlinInstantAdapter"
        )

        plugin(libs.apollo.normalizedCachePlugin.get()) {
            argument("com.apollographql.cache.packageName", packageName.get())
        }

    }

}

buildkonfig {
    packageName = "com.sampletest.shared"
    exposeObjectWithName = "BuildKonfig"

    defaultConfigs {

        buildConfigField(FieldSpec.Type.STRING, "SUPABASE_URL", localProp("SUPABASE_URL"))

        buildConfigField(
            FieldSpec.Type.STRING,
            "SUPABASE_API_KEY",
            localProp("SUPABASE_API_KEY")
        )


    }
}

dependencies {
    androidRuntimeClasspath(libs.jetbrains.uiTooling)
}
