package ru.unit.orchestra_features.common.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY)
annotation class GenerateInteractive(
    val isMutable: Boolean,
    val isToggleable: Boolean
)