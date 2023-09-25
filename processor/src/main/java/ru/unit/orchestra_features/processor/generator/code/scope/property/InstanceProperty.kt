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

class InstanceProperty {

    companion object {

        fun name(featureModel: FeatureModel) = FeatureProperty.name(featureModel)
    }

    fun generate(
        featureScopeModel: FeatureScopeModel,
        featureModel: FeatureModel,
        packageData: PackageData
    ) = PropertySpec.builder(
        name = name(featureModel),
        type = FeatureClass.pack(
            featureModel = featureModel,
            packageData = packageData
        ).asClassName()
    ).apply {
        val pack = "${packageData.name}.${OrchestraClass.name()}.${ScopeProperty.name(featureScopeModel)}"
        val feature = "$pack.${FeatureProperty.name(featureModel)}"

        getter(
            FunSpec.getterBuilder().apply {
                addCode(
                    "return·$feature"
                )
            }.build()
        )
    }.build()
}