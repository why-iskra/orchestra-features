plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.unit.orchestra_features.example.android"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.unit.orchestra_features.example.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }


    // libraryVariants
    applicationVariants.configureEach {
        this@android.sourceSets.getByName(name) {
            kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(project(":common"))
    implementation(project(":interactive:android"))
    ksp(project(":processor"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.fragment:fragment-ktx:1.5.2")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}