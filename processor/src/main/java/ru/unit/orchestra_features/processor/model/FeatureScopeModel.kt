package ru.unit.orchestra_features.processor.model

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import ru.unit.orchestra_features.common.support.utils.delegate.SingleAssignment
import ru.unit.orchestra_features.processor.utils.extension.md5

data class FeatureScopeModel(
    private val rawName: String,
    val dependsOnClasses: List<SClass>,
    val features: List<FeatureModel>,
    val beautifyName: Boolean,
    val clazz: SClass,
    val ksNode: KSAnnotated,
    val originalKSFile: KSFile,
) {
    val isInteractive get() = features.any(FeatureModel::interactive)

    var dependsOn by SingleAssignment<List<FeatureScopeModel>>()

    val id = ("${rawName}_$clazz").md5()

    val name = if (beautifyName) {
        val trimmed = rawName.trim()
            .removeSuffix("Scope")
            .removeSuffix("Feature")
            .removeSuffix("Features")

            .removeSuffix("FeatureScope")
            .removeSuffix("FeaturesScope")
            .removeSuffix("ScopeFeature")
            .removeSuffix("ScopeFeatures")

        "${trimmed}FeatureScope"
    } else {
        rawName.trim()
    }
}