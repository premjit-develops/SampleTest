import com.sampletest.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("composeCompiler").get().get().pluginId)
            apply(libs.findPlugin("kotlinSerialization").get().get().pluginId)
        }


        extensions.configure<KotlinMultiplatformExtension> {

            sourceSets.apply {

                commonMain.dependencies {

                    implementation(libs.findBundle("compose").get())
                    implementation(libs.findBundle("material3").get())
                    implementation(libs.findBundle("navigation3").get())
                    implementation(libs.findLibrary("jetbrains-lifecycleRuntime").get())
                    implementation(libs.findLibrary("jetbrains-lifecycleViewmodel").get())

                }
            }
        }


        dependencies {
            "androidRuntimeClasspath"(libs.findLibrary("jetbrains-uiTooling").get())
        }
    }
}