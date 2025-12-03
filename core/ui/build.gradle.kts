plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.android.library.compose)
    alias(libs.plugins.acall.android.library.jacoco)
}

android {
    namespace = "com.jbrenorv.acall.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
//    api(projects.core.analytics)
    api(projects.core.designsystem)
//    api(projects.core.model)

    implementation(libs.androidx.browser)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
//    androidTestImplementation(projects.core.testing)
}
