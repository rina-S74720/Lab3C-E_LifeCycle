// Top-level build file where you can add configuration options common to all sub-projects/modules.
fun implementation(string: String) {}

plugins {
    alias(libs.plugins.android.application) apply false

    dependencies {

        // Other existing dependencies
        implementation("androidx.navigation:navigation-fragment-ktx:2.9.5")
        implementation("androidx.navigation:navigation-ui-ktx:2.9.5")

    }
}