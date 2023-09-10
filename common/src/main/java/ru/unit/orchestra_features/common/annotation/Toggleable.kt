package ru.unit.orchestra_features.common.annotation

/**
 * Annotation for specifying sources of toggles features
 *
 * Requires: Feature Annotation
 *
 * @param source array of sources of toggles features
 * @param defaultValue default value of toggle
 *
 * @see Feature
 * @see Source
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Toggleable(
    val source: Array<Source> = [],
    val defaultValue: Boolean = false
)