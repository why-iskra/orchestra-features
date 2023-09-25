package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraClass
import ru.unit.orchestra_features.processor.generator.code.orchestra.property.ScopeProperty
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData
import ru.unit.orchestra_features.processor.utils.extension.asClassName

class FeatureInstanceProperty {

    companion object {

        fun name(featureModel: FeatureModel) = FeatureProperty.name(featureModel)
    }

    fun generate(
        featureModel: FeatureModel,
        packageData: PackageData
    ) = PropertySpec.builder(
        name = name(featureModel),
        type = FeatureClass.pack(
            featureModel = featureModel,
            packageData = packageData
        ).asClassName()
    ).apply {
        val feature = "${ScopeInstanceProperty.name()}.${FeatureProperty.name(featureModel)}"

        getter(
            FunSpec.getterBuilder().apply {
                addCode(
                    "return·$feature"
                )
            }.build()
        )
    }.build()
}