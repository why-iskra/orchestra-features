package ru.unit.orchestra_features.processor.generator.code.feature.function

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import ru.unit.orchestra_features.processor.generator.code.feature.property.DependentOnToggleProperty
import ru.unit.orchestra_features.processor.generator.code.feature.property.ToggleProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.FeatureScopeProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.synchronizedCodeBlock

class UpdateFunction {

    companion object {

        fun name() = "update"
    }

    fun generate(featureModel: FeatureModel) = FunSpec.builder(name()).apply {
        addModifiers(KModifier.INTERNAL)

        val code = CodeBlock.synchronizedCodeBlock("this") {
            if (featureModel.dependsOn.isNotEmpty()) {
                add(
                    CodeBlock.builder().apply {
                        beginControlFlow("if (${ToggleProperty.name()})")

                        featureModel.dependsOn.forEach { model ->
                            addStatement(
                                "${OrchestraClass.name()}.${
                                    ScopeProperty.name(
                                        model.scope
                                    )
                                }.${
                                    FeatureScopeProperty.name(
                                        model
                                    )
                                }.${
                                    DependentOnToggleProperty.name(
                                        featureModel
                                    )
                                } = true"
                            )
                        }

                        nextControlFlow("else")

                        featureModel.dependsOn.forEach { model ->
                            addStatement(
                                "${OrchestraClass.name()}.${
                                    ScopeProperty.name(
                                        model.scope
                                    )
                                }.${
                                    FeatureScopeProperty.name(
                                        model
                                    )
                                }.${
                                    DependentOnToggleProperty.name(
                                        featureModel
                                    )
                                } = false"
                            )
                        }

                        endControlFlow()
                    }.build()
                )
            }

            featureModel.dependsOn.forEach { model ->
                addStatement(
                    "${OrchestraClass.name()}.${
                        ScopeProperty.name(
                            model.scope
                        )
                    }.${
                        FeatureScopeProperty.name(
                            model
                        )
                    }.${name()}()"
                )
            }

            addStatement("onStateUpdated()")
        }

        addCode(code)
    }.build()
}