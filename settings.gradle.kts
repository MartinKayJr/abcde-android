@file:Suppress("UnstableApiUsage")


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        // 下载不全需要在这里切换顺序 aliyun下载不了的就换mavenCentral
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}


dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal()
        // 下载不全需要在这里切换顺序 aliyun下载不了的就换mavenCentral
        mavenCentral()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        google()
        maven("https://api.xposed.info/")
    }
}

includeBuild("build-logic")

plugins {
    id("com.gradle.develocity") version "3.17.5"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        val isOffline = providers.provider { gradle.startParameter.isOffline }.getOrElse(false)
        val ci = System.getenv("GITHUB_ACTIONS") == "true"
        publishing {
            onlyIf { System.getenv("GITHUB_ACTIONS") == "true" }
            onlyIf { !isOffline && (it.buildResult.failures.isNotEmpty() || ci) }
        }
    }
}


rootProject.name = "abcde-android"
include(
    ":app"
//    ":libs:abcde",
)
