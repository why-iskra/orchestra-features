buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.8.22-1.0.11")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.2")
    }
}

allprojects {
    group = "ru.unit.orchestra-features"
    version = "1.0.1"

    repositories {
        google()
        mavenCentral()
    }
}