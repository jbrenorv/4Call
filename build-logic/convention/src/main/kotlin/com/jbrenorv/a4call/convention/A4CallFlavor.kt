package com.jbrenorv.a4call.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class A4CallFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: A4CallFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            A4CallFlavor.values().forEach { a4callFlavor ->
                register(a4callFlavor.name) {
                    dimension = a4callFlavor.dimension.name
                    flavorConfigurationBlock(this, a4callFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (a4callFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = a4callFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
