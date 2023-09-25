# orchestra-features
[![](https://jitpack.io/v/why-iskra/orchestra-features.svg)](https://jitpack.io/#why-iskra/orchestra-features)

Orchestra Features library is needed to generate, control and manage features, with minimal code writing costs. It has the functionality of interactive interaction with features in real time.

## Сontents
- Usage
  - Getting started
  - Modules
  - Features
  - Interactive
    - Mutable Data
  - Store mechanism
- To do
- License

## Usage
### Getting started
Orchestra Features is distributed through [jitpack.io](https://jitpack.io/#why-iskra/orchestra-features).
To use it, you need to add the following Gradle dependency to the build.gradle file of your module.

```kotlin
dependencies {
    implementation("com.github.why-iskra.orchestra-features:MODULE:VERSION")
}
```

### Modules
1. **common** - General module, that contains annotations, extensions and another supporting objects
2. **processor** - KSP processor module
3. **interactive-android** - Module, that contains android interactive environment (screens, logic and etc)

### Features
To start using the features, you need to connect the annotations processor module to your build.gradle file

Example for android:
```kotlin
plugins {
    id("com.google.devtools.ksp")
}

android {
    namespace = "your.application.package"
    
    // libraryVariants
    applicationVariants.configureEach {
        this@android.sourceSets.getByName(name) {
            kotlin.srcDir("$buildDir/generated/ksp/$name/kotlin")
        }
    }
    
    ksp { // it's necessary to resolve generational conflicts
        arg("orchestra-features.packagePrefix", namespace!!)
    }
}

dependencies {
    implementation("com.github.why-iskra.orchestra-features:common:VERSION")
    ksp("com.github.why-iskra.orchestra-features:processor:VERSION")
}
```

Minimum setup feature:
```kotlin
@FeatureScope
object TestScope { // Will be created feature scope with TestFeatureScope

    @Feature(mutable = true)
    @Toggleable(
      defaultValue = false,
      source = [Source.EXTERNAL, Source.INTERACTIVE]
    )
    data class TestData( // In TestFeatureScope will be created feature with name testFeature
        var text: String = "hello world!"
    )
}
```

Access to feature:
```kotlin
TestFeatureScope.testFeature.state.isToggled // access to toggle state
TestFeatureScope.testFeature.state.data // access to data

// Or

Orchestra.testFeatureScope.testFeature.state
```

Change feature state:
```kotlin
// value argument will be generated if Toggleable annotation has Source.EXTERNAL 
// data argument will be generated if mutable flag of Feature annotation is true
TestFeatureScope.testFeature.toggle(
    value = true,
    data = TestData()
)
```

### Interactive
To start using the features, you need to connect the interactive platform module to your build.gradle file:
```kotlin
dependencies {
    implementation("com.github.why-iskra.orchestra-features:interactive-(android|jvm):VERSION")
}
```

Before to start using interactive functional, you need to set interactive flag in Feature annotation to true, and inject Feature Scope into interactive environment:

```kotlin
(Android|Jvm)FeatureScopeInjector.inject(Orchestra.interactiveScopes)

// Or

(Android|Jvm)FeatureScopeInjector.inject(TestFeatureScope.instance)
```

Open android interactive environment:
```kotlin
startActivity(Intent(this, OrchestraActivity::class.java))
```

Jvm:
```kotlin
TODO
```

### Mutable Data
TODO

### Store mechanism
TODO

## To do
1. [x] Android interective environment
2. [x] Repository README
3. [ ] Examples for platform
4. [ ] Mutable Data in interactive environment
5. [ ] Desktop interactive environment
6. [ ] Android store mechanism
7. [ ] Desktop store mechanism

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
