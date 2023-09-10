package ru.unit.orchestra_features.processor.generator

import ru.unit.orchestra_features.processor.exception.ProcessorException
import ru.unit.orchestra_features.processor.model.FeatureScopeModel

class Normalizer {

    fun normalizeModels(featureScopeModels: List<FeatureScopeModel>) {
        val featureModels = featureScopeModels.flatMap { model ->
            model.features
        }

        featureScopeModels.forEach { featureScopeModel ->
            featureScopeModel.dependsOn = featureScopeModel.dependsOnClasses.map { clazz ->
                featureScopeModels.singleOrNull { model ->
                    model.clazz == clazz
                } ?: throw ProcessorException(
                    message = "$clazz isn't feature scope",
                    node = featureScopeModel.ksNode
                )
            }

            featureScopeModel.features.forEach { featureModel ->
                featureModel.scope = featureScopeModel
            }
        }

        featureModels.forEach { featureModel ->
            featureModel.dependsOn = featureModel.dependsOnClasses.map { clazz ->
                featureModels.singleOrNull { model ->
                    model.clazz == clazz
                } ?: throw ProcessorException(
                    message = "$clazz isn't feature",
                    node = featureModel.ksNode
                )
            }
        }

        featureModels.forEach { featureModel ->
            featureModel.dependentOn = featureModels.mapNotNull { model ->
                val found = model.dependsOn.find { m ->
                    m.clazz == featureModel.clazz
                }

                if (found != null) {
                    model
                } else {
                    null
                }
            }
        }
    }
}