// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Version catalog aliases
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
