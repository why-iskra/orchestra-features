package ru.unit.orchestra_features.common.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class FeatureScope(
    val name: String = "",
    val dependsOn: Array<KClass<out Any>> = []
)
