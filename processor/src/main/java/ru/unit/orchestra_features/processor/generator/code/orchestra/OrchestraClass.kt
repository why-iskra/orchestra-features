package ru.unit.orchestra_features.processor.generator.code.orchestra

import com.squareup.kotlinpoet.TypeSpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.function.InitFunction
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.InteractiveScopesProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

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
        featureScopeModels.forEach { model ->
            addProperty(
                ScopeProperty().generate(
                    featureScopeModel = model,
                    packageName = packageName
                )
            )
        }

        addInitializerBlock(InitFunction().generate(featureScopeModels))

        val isInteractive = featureScopeModels.any(FeatureScopeModel::isInteractive)

        if (isInteractive) {
            addProperty(InteractiveScopesProperty().generate(featureScopeModels))
        }
    }.build()

}