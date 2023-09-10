package ru.unit.orchestra_features.processor.generator.code.scope

import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureScope
import ru.unit.orchestra_features.processor.generator.code.scope.property.FeatureScopeProperty
import ru.unit.orchestra_features.processor.generator.code.scope.property.FeaturesProperty
import ru.unit.orchestra_features.processor.generator.code.scope.property.InteractiveScopesProperty
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class ScopeClass {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) = featureScopeModel.name

        fun pack(
            model: FeatureScopeModel,
            packageName: String
        ) = ScopeFile.pack(
            model = model,
            packageName = packageName
        ) + ".${name(model)}"
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

        if (featureScopeModel.isInteractive) {
            superclass(InteractiveFeatureScope::class.asClassName())
            addProperty(InteractiveScopesProperty().generate(featureScopeModel))
        }

        addType(
            generateCompanionObject(
                featureScopeModel = featureScopeModel,
                packageName = packageName
            )
        )
    }.build()

    private fun generateCompanionObject(
        featureScopeModel: FeatureScopeModel,
        packageName: String
    ) = TypeSpec.companionObjectBuilder().apply {
        addProperty(
            FeaturesProperty().generate(
                featureScopeModel = featureScopeModel,
                packageName = packageName
            )
        )
    }.build()
}