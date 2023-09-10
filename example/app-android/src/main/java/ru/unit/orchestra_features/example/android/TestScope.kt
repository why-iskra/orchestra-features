package ru.unit.orchestra_features.example.android

import ru.unit.orchestra_features.common.annotation.*

@FeatureScope(name = "TestScope1")
object TestScope {

    @Feature(
        "helloFeature",
        interactive = true,
        mutable = true
    )
    @Toggleable([Source.EXTERNAL, Source.INTERACTIVE])
    @DependsOn([empty1::class])
    data class Hello(
        val test: String = "awd",
        val test2: String = "awd",
        val test3: String = "awd",
        val test4: String = "awd",
        val test5: String = "awd",
        val test6: String = "awd",
        val test7: String = "awd",
        val test8: String = "awd",
        val test9: String = "awd",
    )

    @Feature("helloFeature2", interactive = true)
    @DependsOn([Hello3::class])
    @Toggleable([Source.EXTERNAL, Source.INTERACTIVE])
    class Hello2

    @Feature("helloFeature3")
    class Hello3

    @Feature("f1", interactive = true)
    @DependsOn([empty2::class, empty4::class, empty6::class])
    class empty1

    @Feature("f2", interactive = true)
    class empty2

    @Feature("f3", interactive = true)
    @DependsOn([empty5::class])
    class empty3

    @Feature("f4", interactive = true)
    @DependsOn([empty2::class, empty5::class])
    class empty4

    @Feature("f5", interactive = true)
    @Toggleable([Source.EXTERNAL, Source.INTERACTIVE])
    @DependsOn([empty6::class])
    class empty5

    @Feature("f6", interactive = true)
    class empty6
}
