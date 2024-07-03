import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

fun Project.configureAppSigningConfigsForRelease() = withAndroidApplication {
    if (System.getenv("KEYSTORE_PATH") == null) return@withAndroidApplication
    configure<ApplicationExtension>("android") {
        signingConfigs {
            create("release") {
                storeFile = file(System.getenv("KEYSTORE_PATH"))
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
                enableV2Signing = true
            }
        }
        buildTypes {
            release {
                signingConfig = signingConfigs.findByName("release")
            }
        }
    }
}