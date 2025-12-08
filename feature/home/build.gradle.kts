plugins {
    alias(libs.plugins.acall.android.feature)
    alias(libs.plugins.acall.android.library.compose)
    alias(libs.plugins.acall.android.library.jacoco)
}

android {
    namespace = "com.jbrenorv.acall.feature.home"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.chats)
    implementation(projects.feature.rooms)

//    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
//    androidTestImplementation(projects.core.testing)
}
