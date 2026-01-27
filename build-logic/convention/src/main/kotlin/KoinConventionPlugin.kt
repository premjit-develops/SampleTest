import com.google.devtools.ksp.gradle.KspExtension
import com.sampletest.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension


class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        with(pluginManager) {
            apply(libs.findPlugin("ksp").get().get().pluginId)

        }

        extensions.configure<KotlinMultiplatformExtension> {

            sourceSets.apply {

                commonMain.dependencies {

                    implementation(
                        project.dependencies.platform(
                            libs.findLibrary("koin-bom").get()
                        )
                    )
                    implementation(libs.findBundle("koin").get())
                    api(libs.findLibrary("koin-annotations").get())

                }

                androidMain.dependencies {
                    implementation(libs.findLibrary("koin-android").get())
                }

            }

            sourceSets.named("commonMain").configure {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }

        }


        dependencies {
            add("kspCommonMainMetadata", libs.findLibrary("koin-kspCompiler").get())
            add("kspAndroid", libs.findLibrary("koin-kspCompiler").get())
        }

        tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }
            .configureEach {
                dependsOn("kspCommonMainKotlinMetadata")
            }

        extensions.configure<KspExtension> {
            arg("KOIN_CONFIG_CHECK", "true")
        }

    }

}