import java.util.UUID

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("build-logic.android.application")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val currentBuildUuid = UUID.randomUUID().toString()
println("Current build ID is $currentBuildUuid")


android {
    namespace = "cn.martinkay.abcde"

    defaultConfig {
        applicationId = "cn.martinkay.abcde"
        buildConfigField("String", "BUILD_UUID", "\"$currentBuildUuid\"")
        buildConfigField("long", "BUILD_TIMESTAMP", "${System.currentTimeMillis()}L")

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        resources.excludes.addAll(arrayOf(
            "META-INF/**",
            "kotlin/**",
            "**.bin",
            "kotlin-tooling-metadata.json"
        ))
    }

    kotlinOptions {
        freeCompilerArgs += listOf(
            "-Xno-call-assertions",
            "-Xno-receiver-assertions",
            "-Xno-param-assertions",
        )
    }

    compileOptions {
        sourceCompatibility = Version.java
        targetCompatibility = Version.java
    }

    buildFeatures {
        prefab = true
        aidl = true
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.2"
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.lazy.column.scrollbar)
//    implementation(libs.androidx.compose.foundation)
//    implementation(libs.androidx.compose.foundation.layout)
//    implementation(libs.androidx.compose.foundation.desktop)

    implementation(libs.google.accompanist)
    implementation(libs.abcde.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":libs:fileSelector"))


}