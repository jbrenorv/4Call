plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.android.library.jacoco)
    alias(libs.plugins.acall.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.jbrenorv.acall.core.system"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.core.telecom)
}
