import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "cn.martinkay.buildLogic"

repositories {
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/public")
    maven("https://jitpack.io")
    google()
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.android.tools)
    implementation(libs.kotlin.gradle)
    implementation(libs.eclipse.jgit)
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}