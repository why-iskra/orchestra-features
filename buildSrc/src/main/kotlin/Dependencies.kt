object Dependencies {

    object GradlePlugins {

        val gradle = Dependency(
            group = "com.android.tools.build",
            module = "gradle",
            version = Version.GRADLE
        ).resolve()

        val kotlin = Dependency(
            group = "org.jetbrains.kotlin",
            module = "kotlin-gradle-plugin",
            version = Version.KOTLIN
        ).resolve()

        val ksp = Dependency(
            group = "com.google.devtools.ksp",
            module = "symbol-processing-gradle-plugin",
            version = Version.KSP
        ).resolve()

        val navigation = Dependency(
            group = "androidx.navigation",
            module = "navigation-safe-args-gradle-plugin",
            version = Version.NAVIGATION
        ).resolve()
    }

    val coroutines = Dependency(
        group = "org.jetbrains.kotlinx",
        module = "kotlinx-coroutines-core",
        version = Version.COROUTINES
    ).resolve()

    val navigationFragment = Dependency(
        group = "androidx.navigation",
        module = "navigation-fragment-ktx",
        version = Version.NAVIGATION
    ).resolve()

    val navigationUi = Dependency(
        group = "androidx.navigation",
        module = "navigation-ui",
        version = Version.NAVIGATION
    ).resolve()

    val appcompat = Dependency(
        group = "androidx.appcompat",
        module = "navigation-ui",
        version = Version.APPCOMPAT
    ).resolve()

    val fragment = Dependency(
        group = "androidx.fragment",
        module = "fragment-ktx",
        version = Version.FRAGMENT
    ).resolve()

    val material = Dependency(
        group = "com.google.android.material",
        module = "material",
        version = Version.MATERIAL
    ).resolve()

    val constraintlayout = Dependency(
        group = "androidx.constraintlayout",
        module = "constraintlayout",
        version = Version.CONSTRAINT_LAYOUT
    ).resolve()

    val lifecycle = Dependency(
        group = "androidx.lifecycle",
        module = "lifecycle-runtime-ktx",
        version = Version.LIFECYCLE
    ).resolve()

    val ksp = Dependency(
        group = "com.google.devtools.ksp",
        module = "symbol-processing-api",
        version = Version.KSP
    ).resolve()

    val kotlinpoet = Dependency(
        group = "com.squareup",
        module = "kotlinpoet",
        version = Version.KOTLINPOET
    ).resolve()

    val kotlinpoetKsp = Dependency(
        group = "com.squareup",
        module = "kotlinpoet-ksp",
        version = Version.KOTLINPOET
    ).resolve()
}