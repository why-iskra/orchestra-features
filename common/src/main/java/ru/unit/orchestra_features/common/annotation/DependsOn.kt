package ru.unit.orchestra_features.common.annotation

import kotlin.reflect.KClass

/**
 * Annotation for specifying feature dependencies
 *
 * Requires: Feature Annotation
 *
 * @param features array of classes with Feature annotation which feature depends on
 *
 * @see Feature
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DependsOn(
    val features: Array<KClass<out Any>> = [],
)