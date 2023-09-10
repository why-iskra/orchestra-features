package ru.unit.orchestra_features.processor.generator.code.feature

import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class FeatureFile {

    fun generate(
        featureScopeModels: List<FeatureScopeModel>,
        packageName: String
    ): List<FileSpec> {
        val features = featureScopeModels.flatMap { model -> model.features }

        return features.map { model ->
            FileSpec.get(
                packageName = packageName,
                typeSpec = FeatureClass().generate(model)
            )
        }
    }
}