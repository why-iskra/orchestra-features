package ru.unit.orchestra_features.processor.generator.code.orchestra.function

import com.squareup.kotlinpoet.CodeBlock
import ru.unit.orchestra_features.processor.generator.code.scope.property.FeatureProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class InitFunction {

    fun generate(
        featureScopeModels: List<FeatureScopeModel>
    ) = CodeBlock.builder().apply {
        featureScopeModels.flatMap { model ->
            model.features
        }.forEach { model ->
            addStatement("${ScopeProperty.name(model.scope)}.${FeatureProperty.name(model)}")
        }
    }.build()

}