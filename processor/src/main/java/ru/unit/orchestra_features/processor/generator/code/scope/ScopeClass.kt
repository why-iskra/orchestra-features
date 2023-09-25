package ru.unit.orchestra_features.processor.generator.code.scope

import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureScope
import ru.unit.orchestra_features.processor.generator.code.scope.property.FeatureInstanceProperty
import ru.unit.orchestra_features.processor.generator.code.scope.property.FeatureProperty
import ru.unit.orchestra_features.processor.generator.code.scope.property.InteractiveScopeProperty
import ru.unit.orchestra_features.processor.generator.code.scope.property.ScopeInstanceProperty
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData

class ScopeClass {

    companion object {

        fun name(featureScopeModel: FeatureScopeModel) = featureScopeModel.name

        fun pack(
            model: FeatureScopeModel,
            packageData: PackageData,
        ) = ScopeFile.pack(
            model = model,
            packageData = packageData
        ) + ".${name(model)}"
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        packageData: PackageData,
    ) = TypeSpec.classBuilder(
        name = name(featureScopeModel)
    ).apply {
        addProperties(
            featureScopeModel.features.map { featureModel ->
                FeatureProperty().generate(
                    featureModel = featureModel,
                    packageData = packageData
                )
            }
        )

        if (featureScopeModel.isInteractive) {
            superclass(InteractiveFeatureScope::class.asClassName())
            addProperty(InteractiveScopeProperty().generate(featureScopeModel))
        }

        addType(
            generateCompanionObject(
                featureScopeModel = featureScopeModel,
                packageData = packageData
            )
        )
    }.build()

    private fun generateCompanionObject(
        featureScopeModel: FeatureScopeModel,
        packageData: PackageData
    ) = TypeSpec.companionObjectBuilder().apply {
        addProperty(
            ScopeInstanceProperty().generate(
                featureScopeModel = featureScopeModel,
                packageData = packageData
            )
        )

        featureScopeModel.features.forEach { model ->
            addProperty(
                FeatureInstanceProperty().generate(
                    featureModel = model,
                    packageData = packageData
                )
            )
        }
    }.build()
}