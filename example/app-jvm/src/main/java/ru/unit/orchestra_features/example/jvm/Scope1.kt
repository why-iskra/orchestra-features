package ru.unit.orchestra_features.example.jvm

import ru.unit.orchestra_features.common.annotation.*

@FeatureScope(dependsOn = [Scope2::class])
class Scope1 {

    @Feature(name = "awd", description = "cvmkmvc", mutable = true, interactive = true)
    @DependsOn([sc.fsc::class, f2::class])
    @Toggleable(
        defaultValue = true,
        source = [Source.EXTERNAL, Source.INTERACTIVE]
    )
    data class f1(
        val ste: String = "awd\"\rawd",
        val sawd: Boolean = false
    )

    @Feature
    @Toggleable(
        defaultValue = true,
        source = [Source.EXTERNAL]
    )
    class f2

    @Feature
    class f3

    @FeatureScope
    object sc {

        @Feature
        class fsc
    }
}