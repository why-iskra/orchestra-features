plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.unit.orchestra-features"
            artifactId = "processor"
            version = "1.0.1"

            from(components["java"])
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.22-1.0.11")
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")
}