package ru.unit.orchestra_features.processor.model

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import ru.unit.orchestra_features.common.support.utils.delegate.SingleAssignment
import ru.unit.orchestra_features.processor.utils.extension.md5

data class FeatureScopeModel(
    val name: String,
    val dependsOnClasses: List<SClass>,
    val features: List<FeatureModel>,
    val clazz: SClass,
    val ksNode: KSAnnotated,
    val originalKSFile: KSFile,
) {
    val isInteractive get() = features.any(FeatureModel::interactive)

    var dependsOn by SingleAssignment<List<FeatureScopeModel>>()

    val id = ("${name}_$clazz").md5()
}