plugins {
    alias(libs.plugins.acall.android.feature)
    alias(libs.plugins.acall.android.library.compose)
    alias(libs.plugins.acall.android.library.jacoco)
}

android {
    namespace = "com.jbrenorv.acall.feature.chat"
}

dependencies {
//    implementation(projects.core.data)
//
//    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
//    androidTestImplementation(projects.core.testing)
}
