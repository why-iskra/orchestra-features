package ru.unit.orchestra_features.processor.generator.code.feature

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import ru.unit.orchestra_features.common.annotation.Source
import ru.unit.orchestra_features.processor.generator.code.feature.function.InteractiveToggleFunction
import ru.unit.orchestra_features.processor.generator.code.feature.function.ToggleFunction
import ru.unit.orchestra_features.processor.generator.code.feature.function.UpdateFunction
import ru.unit.orchestra_features.processor.generator.code.feature.property.*
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.processor.utils.PackageData

class FeatureClass {

    companion object {

        fun name(featureModel: FeatureModel) = "Feature${featureModel.id}"

        fun pack(
            featureModel: FeatureModel,
            packageData: PackageData
        ) = "${packageData.name}.${name(featureModel)}"
    }

    fun generate(model: FeatureModel) = TypeSpec.classBuilder(
        name = name(model)
    ).apply {
        val clazz = Feature::class.asTypeName()
            .parameterizedBy(model.clazz.asClassName())

        superclass(clazz)

        if (model.toggleable.source.contains(Source.INTERACTIVE)) {
            addSuperinterface(Feature.InteractiveToggleable::class.asTypeName())
        }

        addProperties(generateProperties(model))
        addFunctions(generateFunctions(model))

        addInitializerBlock(
            CodeBlock.builder().apply {
                addStatement("${UpdateFunction.name()}()")
            }.build()
        )
    }.build()

    private fun generateFunctions(model: FeatureModel) = listOfNotNull(
        UpdateFunction().generate(model),
        ToggleFunction().generate(model).takeIf {
            model.toggleable.source.contains(Source.EXTERNAL)
        },
        InteractiveToggleFunction().generate().takeIf {
            model.toggleable.source.contains(Source.INTERACTIVE)
        }
    )

    private fun generateProperties(featureModel: FeatureModel): List<PropertySpec> {
        val dependsOnToggleProperties = featureModel.dependsOn.map { model ->
            DependsOnToggleProperty().generate(model)
        }

        val dependentOnToggleProperties = featureModel.dependentOn.map { model ->
            DependentOnToggleProperty().generate(model)
        }

        val manualToggleProperty = ManualToggleProperty()
            .generate(featureModel)

        val toggleProperty = ToggleProperty().generate(featureModel)

        val stateProperty = StateProperty().generate(featureModel)

        val dataProperty = DataProperty().generate(featureModel)

        return listOfNotNull(
            stateProperty,
            dataProperty,
            toggleProperty,
            manualToggleProperty,
        ) + dependsOnToggleProperties + dependentOnToggleProperties
    }
}