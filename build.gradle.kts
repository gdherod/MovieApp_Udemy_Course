// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val nav_version = "2.5.3"
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("androidx.navigation.safeargs.kotlin") version nav_version apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
}