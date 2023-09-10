package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import ru.unit.orchestra_features.processor.utils.extension.synchronizedCodeBlock

class StateProperty {

    companion object {

        fun name() = "state"
    }

    fun generate(featureModel: FeatureModel) = PropertySpec.builder(
        name = name(),
        type = Feature.State::class.asTypeName()
            .parameterizedBy(featureModel.clazz.asClassName())
    ).apply {
        addModifiers(KModifier.OVERRIDE)

        val toggles = featureModel.dependsOn.map(
            DependsOnToggleProperty::name
        ) + listOf(
            ToggleProperty.name()
        )

        getter(
            FunSpec.getterBuilder().apply {
                addCode(
                    CodeBlock.synchronizedCodeBlock("this") {
                        addStatement(
                            "return %T(${toggles.joinToString(" && ")}, ${DataProperty.name()})",
                            Feature.State::class.asTypeName().parameterizedBy(featureModel.clazz.asClassName())
                        )
                    }
                )
            }.build()
        )
    }.build()
}