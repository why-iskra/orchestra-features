package ru.unit.orchestra_features.common.annotation

import kotlin.reflect.KClass

/**
 * Annotation for marking scope of feature classes
 *
 * @param name name of scope (if empty, the class name is used)
 * @param dependsOn array of classes with ScopeFeature annotation which scope depends on ()
 * @param beautifyName transforms name without changing meaning
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class FeatureScope(
    val name: String = "",
    val dependsOn: Array<KClass<out Any>> = [],
    val beautifyName: Boolean = true,
)
