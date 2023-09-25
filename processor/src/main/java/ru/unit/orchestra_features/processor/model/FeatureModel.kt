package ru.unit.orchestra_features.processor.model

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import ru.unit.orchestra_features.common.annotation.Toggleable
import ru.unit.orchestra_features.common.support.utils.delegate.SingleAssignment
import ru.unit.orchestra_features.processor.utils.extension.md5

data class FeatureModel(
    private val rawName: String,
    val description: String?,
    val mutable: Boolean,
    val interactive: Boolean,
    val dependsOnClasses: List<SClass>,
    val toggleable: Toggleable,
    val beautifyName: Boolean,
    val clazz: SClass,
    val ksNode: KSAnnotated,
    val originalKSFile: KSFile,
) {
    var scope by SingleAssignment<FeatureScopeModel>()
    var dependsOn by SingleAssignment<List<FeatureModel>>()
    var dependentOn by SingleAssignment<List<FeatureModel>>()

    val id = ("${rawName}_$clazz").md5()

    val name = if (beautifyName) {
        val trimmed = rawName.trim()
            .removeSuffix("Data")
            .removeSuffix("Feature")
            .removeSuffix("DataFeature")
            .removeSuffix("FeatureData")

        "${trimmed}Feature"
    } else {
        rawName.trim()
    }
}