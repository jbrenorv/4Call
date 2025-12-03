package com.jbrenorv.acall.convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class ACallBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
