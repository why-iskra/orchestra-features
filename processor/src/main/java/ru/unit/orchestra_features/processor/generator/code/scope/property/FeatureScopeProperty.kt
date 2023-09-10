package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureClass
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class FeatureScopeProperty {

    companion object {

        fun name(featureModel: FeatureModel) = featureModel.name
    }

    fun generate(
        featureModel: FeatureModel,
        packageName: String
    ) = PropertySpec.builder(
        name = name(featureModel),
        type = "${packageName}.${FeatureClass.name(featureModel)}".asClassName()
    ).apply {
        val className = "${packageName}.${FeatureClass.name(featureModel)}".asClassName()

        featureModel.description?.let(::addKdoc)

        delegate("lazy { %T() }", className)
    }.build()
}