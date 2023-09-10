package ru.unit.orchestra_features.processor.generator.code.orchestra

import com.squareup.kotlinpoet.TypeSpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.function.InitFunction
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.InteractiveScopesProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ModuleProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.common.support.interactive.InteractiveOrchestra

class OrchestraClass {

    companion object {
        fun name() = "Orchestra"
    }

    fun generate(
        featureScopeModels: List<FeatureScopeModel>,
        packageName: String
    ) = TypeSpec.objectBuilder(
        name = name()
    ).apply {
        val isInteractive = featureScopeModels
            .flatMap(FeatureScopeModel::features)
            .any(FeatureModel::interactive)

        featureScopeModels.forEach { model ->
            addType(
                ScopeClass().generate(
                    featureScopeModel = model,
                    packageName = packageName
                )
            )
            addProperty(ScopeProperty().generate(model))
        }

        if (isInteractive) {
            superclass(InteractiveOrchestra::class)

            addProperty(InteractiveScopesProperty().generate(featureScopeModels))
            addProperty(ModuleProperty().generate(packageName))
        }

        addInitializerBlock(InitFunction().generate(featureScopeModels))
    }.build()

}