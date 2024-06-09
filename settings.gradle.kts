pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        jcenter() {
            content {
                includeModule("com.theartofdev.edmodo", "android-image-cropper")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        jcenter() {
            content {
                includeModule("com.theartofdev.edmodo", "android-image-cropper")
            }
        }
    }
}

rootProject.name = "Dance Studio App"
include(":app")
 