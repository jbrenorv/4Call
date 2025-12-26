plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.hilt)
}

android {
    namespace = "com.jbrenorv.acall.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
