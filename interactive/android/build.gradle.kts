plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("maven-publish")
}

android {
    namespace = "ru.unit.orchestra_features.interactive.android"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    resourcePrefix("ofia_")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = ProjectInfo.GROUP_ID
                artifactId = ProjectInfo.ArtifactId.InteractiveAndroid.name
                version = ProjectInfo.VERSION

                from(components["release"])
            }
        }
    }
}

dependencies {
    api(project(":common"))

    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.fragment)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.lifecycle)
}