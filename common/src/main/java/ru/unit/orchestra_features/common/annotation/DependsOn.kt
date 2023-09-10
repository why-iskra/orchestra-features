package ru.unit.orchestra_features.common.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DependsOn(
    val features: Array<KClass<out Any>> = [],
)