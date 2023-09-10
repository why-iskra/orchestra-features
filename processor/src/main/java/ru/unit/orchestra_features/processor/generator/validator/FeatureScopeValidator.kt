package ru.unit.orchestra_features.processor.generator.validator

import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class FeatureScopeValidator {

    fun validate(featureScopeModels: List<FeatureScopeModel>) {
        checkNameDuplicates(featureScopeModels)

        featureScopeModels.forEach { model ->
            checkInheritanceRecursion(model)

            FeatureValidator().validate(model.features)
        }
    }

    private fun checkNameDuplicates(models: List<FeatureScopeModel>) {
        models.forEach { model ->
            models.singleOrNull { m ->
                m.name == model.name
            } ?: throw ProcessorException(
                message = "Duplicate of feature scope name ${model.name}",
                node = model.ksNode
            )
        }
    }

    private fun checkInheritanceRecursion(
        currentModel: FeatureScopeModel,
        chain: List<FeatureScopeModel> = emptyList(),
    ) {
        if (currentModel in chain) {
            val stringChain = (chain + listOf(currentModel)).joinToString(" -> ") { model ->
                model.name
            }

            throw ProcessorException(
                message = "Inheritance recursion of feature scopes ($stringChain)",
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