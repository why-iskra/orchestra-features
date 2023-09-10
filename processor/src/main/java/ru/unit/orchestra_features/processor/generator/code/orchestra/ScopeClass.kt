package ru.unit.orchestra_features.processor.generator.code.orchestra

import com.squareup.kotlinpoet.TypeSpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.FeatureScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class ScopeClass {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) = "FeatureScope${featureScopeModel.id}"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageName: String
    ) = TypeSpec.classBuilder(
        name = name(featureScopeModel)
    ).apply {
        addProperties(
            featureScopeModel.features.map { featureModel ->
                FeatureScopeProperty().generate(
                    featureModel = featureModel,
                    packageName = packageName
                )
            }
        )
    }.build()
}