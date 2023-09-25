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
            groupId = ProjectInfo.GROUP_ID
            artifactId = ProjectInfo.ArtifactId.Processor.name
            version = ProjectInfo.VERSION

            from(components["java"])
        }
    }
}

dependencies {
    implementation(project(":common"))

    implementation(Dependencies.ksp)
    implementation(Dependencies.kotlinpoet)
    implementation(Dependencies.kotlinpoetKsp)
}