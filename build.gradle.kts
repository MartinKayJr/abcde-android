
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("build-logic.root-project")
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
