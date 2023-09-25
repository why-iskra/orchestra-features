package ru.unit.orchestra_features.processor.generator.code.scope.property

import com.squareup.kotlinpoet.PropertySpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureClass
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.utils.PackageData
import ru.unit.orchestra_features.processor.utils.extension.asClassName
import java.util.*

class FeatureProperty {

    companion object {

        fun name(featureModel: FeatureModel) =
            featureModel.name.replaceFirstChar { c -> c.lowercase(Locale.getDefault()) }
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
        val className = FeatureClass.pack(
            featureModel = featureModel,
            packageData = packageData
        ).asClassName()

        featureModel.description?.let(::addKdoc)

        delegate("lazy·{·%T()·}", className)
    }.build()
}