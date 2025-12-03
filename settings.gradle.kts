pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "acall"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core")
include(":core:network")
include(":core:ui")
include(":core:designsystem")
include(":core:datastore-proto")
include(":core:datastore")
include(":core:model")
include(":core:data")

include(":feature")
include(":feature:chat")
include(":feature:chats")
include(":feature:room")
include(":feature:rooms")
