plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")

    application
}

group = "ru.unit.orchestra_features.example.jvm"
version = "unspecified"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin.sourceSets.main {
    kotlin.srcDirs(file("$buildDir/generated/ksp/main/kotlin"))
}

application {
    mainClass.set("ru.unit.orchestra_features.MainKt")
}

dependencies {
    implementation(project(":common"))
    ksp(project(":processor"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}