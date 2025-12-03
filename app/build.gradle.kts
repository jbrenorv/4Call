import com.jbrenorv.acall.convention.ACallBuildType

plugins {
    alias(libs.plugins.acall.android.application)
    alias(libs.plugins.acall.android.application.compose)
    alias(libs.plugins.acall.android.application.flavors)
    alias(libs.plugins.acall.android.application.jacoco)
    alias(libs.plugins.acall.android.application.firebase)
    alias(libs.plugins.acall.hilt)
    alias(libs.plugins.google.osslicenses)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jbrenorv.acall"

    defaultConfig {
        applicationId = "com.jbrenorv.acall"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ACallBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            applicationIdSuffix = ACallBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // To publish on the Play store a private signing key is required
            // signingConfig = ...
            baselineProfile.automaticGenerationDuringBuild = true
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(projects.feature.chat)
    implementation(projects.feature.chats)
    implementation(projects.feature.room)
    implementation(projects.feature.rooms)

//    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
//    implementation(projects.core.analytics)
//    implementation(projects.sync.work)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)
//    debugImplementation(projects.uiTestHiltManifest)

    kspTest(libs.hilt.compiler)

//    testImplementation(projects.core.dataTest)
//    testImplementation(projects.core.datastoreTest)
    testImplementation(libs.hilt.android.testing)
//    testImplementation(projects.sync.syncTest)
    testImplementation(libs.kotlin.test)

    testDemoImplementation(libs.androidx.navigation.testing)
    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)
//    testDemoImplementation(projects.core.screenshotTesting)
//    testDemoImplementation(projects.core.testing)

//    androidTestImplementation(projects.core.testing)
//    androidTestImplementation(projects.core.dataTest)
//    androidTestImplementation(projects.core.datastoreTest)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
//    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test)

//    baselineProfile(projects.benchmarks)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false

    // Make use of Dex Layout Optimizations via Startup Profiles
    dexLayoutOptimization = true
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}
