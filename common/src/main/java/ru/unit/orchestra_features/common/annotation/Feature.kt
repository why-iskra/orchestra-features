package ru.unit.orchestra_features.common.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Feature(
    val name: String = "",
    val description: String = "",
    val mutable: Boolean = false,
    val interactive: Boolean = false,
)
