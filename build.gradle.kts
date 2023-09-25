buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.GradlePlugins.gradle)
        classpath(Dependencies.GradlePlugins.kotlin)
        classpath(Dependencies.GradlePlugins.ksp)
        classpath(Dependencies.GradlePlugins.navigation)
    }
}

allprojects {
    group = ProjectInfo.GROUP_ID
    version = ProjectInfo.VERSION

    repositories {
        google()
        mavenCentral()
    }
}