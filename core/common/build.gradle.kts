plugins {
    alias(libs.plugins.acall.jvm.library)
    alias(libs.plugins.acall.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
