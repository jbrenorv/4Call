plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.android.library.jacoco)
    alias(libs.plugins.acall.hilt)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "com.jbrenorv.acall.core.datastore"
}

dependencies {
    api(libs.androidx.dataStore)
    api(projects.core.datastoreProto)
    api(projects.core.model)

//    implementation(projects.core.common)

//    testImplementation(projects.core.datastoreTest)
    testImplementation(libs.kotlinx.coroutines.test)
}
