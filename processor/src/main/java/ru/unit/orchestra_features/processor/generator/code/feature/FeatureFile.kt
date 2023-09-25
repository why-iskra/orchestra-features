package ru.unit.orchestra_features.processor.generator.code.feature

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel
import ru.unit.orchestra_features.processor.utils.PackageData

class FeatureFile {

    fun generate(
        featureScopeModels: List<FeatureScopeModel>,
        packageData: PackageData
    ): List<FileSpec> {
        val features = featureScopeModels.flatMap { model -> model.features }

        return features.map { model ->
            FileSpec.get(
                packageName = packageData.name,
                typeSpec = FeatureClass().generate(model)
            )
        }
    }
}