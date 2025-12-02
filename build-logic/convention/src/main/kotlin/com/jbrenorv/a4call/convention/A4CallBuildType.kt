package com.jbrenorv.a4call.convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class A4CallBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
