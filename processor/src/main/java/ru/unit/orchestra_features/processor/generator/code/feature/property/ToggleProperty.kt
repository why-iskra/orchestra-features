package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.model.FeatureModel

class ToggleProperty {

    companion object {

        fun name() = "toggle"
    }

    fun generate(featureModel: FeatureModel) = PropertySpec.builder(
        name = name(),
        type = Boolean::class.java,
        KModifier.PRIVATE
    ).apply {
        val dependentOnToggles = featureModel.dependentOn
            .map(DependentOnToggleProperty.Companion::name)

        val manualToggle = ManualToggleProperty.name()

        val toggles = listOfNotNull(
            manualToggle
        ).plus(dependentOnToggles)

        getter(
            FunSpec.getterBuilder()
                .addStatement("return·${toggles.joinToString("·||·")}")
                .build()
        )
    }.build()
}