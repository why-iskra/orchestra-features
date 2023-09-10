package ru.unit.orchestra_features.processor.generator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import ru.unit.orchestra_features.processor.generator.code.feature.FeatureFile
import ru.unit.orchestra_features.processor.generator.code.orchestra.OrchestraFile
import ru.unit.orchestra_features.processor.generator.validator.FeatureScopeValidator
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class Code(
    private val environment: SymbolProcessorEnvironment,
    private val packageName: String
) {

    fun generateFiles(featureScopeModels: List<FeatureScopeModel>): List<FileSpec> {
        ru.unit.orchestra_features.processor.generator.Normalizer().normalizeModels(featureScopeModels)
        FeatureScopeValidator().validate(featureScopeModels)

        return OrchestraFile().generate(
            featureScopeModels = featureScopeModels,
            packageName = packageName
        ) + FeatureFile().generate(
            featureScopeModels = featureScopeModels,
            packageName = packageName
        )
    }
}