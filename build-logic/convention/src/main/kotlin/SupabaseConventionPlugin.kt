import com.sampletest.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension


class SupabaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        extensions.configure<KotlinMultiplatformExtension> {

            sourceSets.apply {

                commonMain.dependencies {

                    implementation(
                        project.dependencies.platform(
                            libs.findLibrary("supabase-bom").get()
                        )
                    )
                    implementation(libs.findBundle("supabase").get())
                    implementation(libs.findLibrary("apollo-normalizedCache").get())
                    implementation(libs.findLibrary("apollo-normalizedCacheSqlite").get())

                }

                androidMain.dependencies {
                    implementation(libs.findLibrary("ktor-clientOkhttp").get())
                }
            }
        }
    }
}