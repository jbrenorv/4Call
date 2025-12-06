plugins {
    alias(libs.plugins.acall.android.library)
    alias(libs.plugins.acall.android.library.jacoco)
    alias(libs.plugins.acall.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.jbrenorv.acall.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true

    buildTypes {
        release {
            consumerProguardFiles("proguard-data-rules.pro")
        }
    }
}

dependencies {
//    api(projects.core.common)
//    api(projects.core.database)
    api(projects.core.datastore)
//    api(projects.core.network)

//    implementation(projects.core.analytics)
//    implementation(projects.core.notifications)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
//    testImplementation(projects.core.datastoreTest)
//    testImplementation(projects.core.testing)
}
