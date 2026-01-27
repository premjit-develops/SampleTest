package com.sampletest.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project


/*
fun Project.commonAndroidConfiguration(
    extension: KotlinMultiplatformAndroidLibraryExtension,
) = extension.apply {

    val moduleName = path.split(":").drop(2).joinToString(".")
    namespace = if (moduleName.isNotEmpty()) "com.nitivakya.$moduleName" else "com.nitivakya"

    compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

    minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()

    androidResources {
        enable = true
    }

    withHostTestBuilder {
    }

    withDeviceTestBuilder {
        sourceSetTreeName = "test"
    }.configure {
        instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}
*/
