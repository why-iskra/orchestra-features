package ru.unit.orchestra_features.processor.model

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import ru.unit.orchestra_features.common.annotation.Toggleable
import ru.unit.orchestra_features.common.support.utils.delegate.SingleAssignment
import ru.unit.orchestra_features.processor.utils.extension.md5

data class FeatureModel(
    val name: String,
    val description: String?,
    val mutable: Boolean,
    val interactive: Boolean,
    val dependsOnClasses: List<SClass>,
    val toggleable: Toggleable,
    val clazz: SClass,
    val ksNode: KSAnnotated,
    val originalKSFile: KSFile,
) {
    var scope by SingleAssignment<FeatureScopeModel>()
    var dependsOn by SingleAssignment<List<FeatureModel>>()
    var dependentOn by SingleAssignment<List<FeatureModel>>()

    val id = ("${name}_$clazz").md5()
}