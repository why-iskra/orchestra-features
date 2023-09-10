package ru.unit.orchestra_features.processor.generator.code.feature.property

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.FeatureScopeProperty
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureModel

class DependsOnToggleProperty {

    companion object {

        fun name(featureModel: FeatureModel) = "dependsOn${featureModel.id}Toggle"
    }

    fun generate(featureModel: FeatureModel): PropertySpec {

        return PropertySpec.builder(
            name = name(featureModel),
            type = Boolean::class.java,
            KModifier.PRIVATE
        ).apply {
            getter(
                FunSpec.getterBuilder()
                    .addStatement(
                        "return ${OrchestraClass.name()}.${
                            ScopeProperty.name(
                                featureModel.scope
                            )
                        }.${
                            FeatureScopeProperty.name(
                                featureModel
                            )
                        }.${StateProperty.name()}.isToggled"
                    )
                    .build()
            )
        }.build()
    }
}