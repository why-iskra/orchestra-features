package ru.unit.orchestra_features.common.annotation

/**
 * Annotation for marking feature class
 *
 * Requires: Feature class must be inside class which marked with FeatureScope annotation
 *
 * @param name name of feature (if empty, the class name is used)
 * @param description description of feature (inserted in comments to generated feature)
 * @param mutable allow/deny changing implementation of class in generated feature
 * @param interactive allow/deny display in interactive environment
 * @param beautifyName transforms name without changing meaning
 *
 * @see FeatureScope
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Feature(
    val name: String = "",
    val description: String = "",
    val mutable: Boolean = false,
    val interactive: Boolean = false,
    val beautifyName: Boolean = true,
)
