import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

object Version {
    val java = JavaVersion.VERSION_17

    const val compileSdkVersion = 34
    val buildToolsVersion = findBuildToolsVersion()
    const val minSdk = 26
    const val targetSdk = 34
    const val versionName = "1.0.1"

    private const val defaultNdkVersion = "25.2.9519653"
    private const val defaultCMakeVersion = "3.22.1"



    fun getNdkVersion(project: Project): String {
        val prop = getLocalProperty(project, "abcde.override.ndk.version")
        val env = getEnvVariable("ABCDE_OVERRIDE_NDK_VERSION")
        if (!prop.isNullOrEmpty() && !env.isNullOrEmpty()) {
            throw IllegalStateException("Cannot set both ABCDE_OVERRIDE_NDK_VERSION and abcde.override.ndk.version")
        }
        return prop ?: env ?: defaultNdkVersion
    }

    fun getCMakeVersion(project: Project): String {
        val prop = getLocalProperty(project, "abcde.override.cmake.version")
        val env = getEnvVariable("ABCDE_OVERRIDE_CMAKE_VERSION")
        if (!prop.isNullOrEmpty() && !env.isNullOrEmpty()) {
            throw IllegalStateException("Cannot set both ABCDE_OVERRIDE_CMAKE_VERSION and abcde.override.cmake.version")
        }
        return prop ?: env ?: defaultCMakeVersion
    }

    private fun getLocalProperty(project: Project, propertyName: String): String? {
        val rootProject = project.rootProject
        val localProp = File(rootProject.projectDir, "local.properties")
        if (!localProp.exists()) {
            return null
        }
        val localProperties = Properties()
        localProp.inputStream().use {
            localProperties.load(it)
        }
        return localProperties.getProperty(propertyName, null)
    }

    private fun getEnvVariable(name: String): String? {
        return System.getenv(name)
    }

    private fun findBuildToolsVersion(): String {
        val defaultBuildToolsVersion = "34.0.0" // AGP 8.2.0 need Build Tools 34.0.0
        return File(System.getenv("ANDROID_HOME"), "build-tools").listFiles()?.filter { it.isDirectory }?.maxOfOrNull { it.name }?.also { println("Using build tools version $it") }
            ?: defaultBuildToolsVersion
    }
}