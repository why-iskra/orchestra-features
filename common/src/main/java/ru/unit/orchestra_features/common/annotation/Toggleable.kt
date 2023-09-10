package ru.unit.orchestra_features.common.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Toggleable(
    val source: Array<Source> = [],
    val defaultValue: Boolean = false
)