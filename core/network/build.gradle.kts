plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.android.library.jacoco)
    alias(libs.plugins.acall.hilt)
}

android {
    namespace = "com.jbrenorv.acall.core.network"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
