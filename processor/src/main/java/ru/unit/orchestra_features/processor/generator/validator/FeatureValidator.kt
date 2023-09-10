package ru.unit.orchestra_features.processor.generator.validator

import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureModel
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class FeatureValidator {
    fun validate(featureModels: List<FeatureModel>) {
        checkNameDuplicates(featureModels)

        featureModels.forEach { model ->
            checkInheritanceVisibility(model)
            checkInheritanceRecursion(model)
        }
    }

    private fun checkNameDuplicates(models: List<FeatureModel>) {
        models.forEach { model ->
            models.singleOrNull { m ->
                m.name == model.name
            } ?: throw ProcessorException(
                message = "Duplicate of feature name ${model.name}",
                node = model.ksNode
            )
        }
    }

    private fun checkInheritanceVisibility(featureModel: FeatureModel) {
        val availableFeatures = getScopesList(featureModel.scope).flatMap { model ->
            model.features
        }

        featureModel.dependsOn.forEach { model ->
            availableFeatures.find { featureModel ->
                featureModel == model
            } ?: throw ProcessorException(
                message = "Feature ${model.name} isn't reachable",
                node = featureModel.ksNode
            )
        }
    }

    private fun getScopesList(featureScopeModel: FeatureScopeModel): List<FeatureScopeModel> =
        featureScopeModel.dependsOn.flatMap(::getScopesList) + listOf(featureScopeModel)

    private fun checkInheritanceRecursion(
        currentModel: FeatureModel,
        chain: List<FeatureModel> = emptyList(),
    ) {
        if (currentModel in chain) {
            val stringChain = (chain + listOf(currentModel)).joinToString(" -> ") { model ->
                model.name
            }

            throw ProcessorException(
                message = "Inheritance recursion of feature ($stringChain)",
                node = currentModel.ksNode
            )
        }

        val dependencies = currentModel.dependsOn

        dependencies.forEach { model ->
            checkInheritanceRecursion(
                currentModel = model,
                chain = chain + listOf(currentModel)
            )
        }
    }
}