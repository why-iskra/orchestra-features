package ru.unit.orchestra_features.interactive.android.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.interactive.android.AndroidFeatureScopeInjector
import ru.unit.orchestra_features.interactive.android.model.FeatureModel
import kotlin.reflect.KProperty

class FeatureModelsProducer(
    elementsFlow: Flow<List<ElementData>>
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val modelsFlow = elementsFlow.flatMapLatest { elements ->
        val allFeatures = AndroidFeatureScopeInjector.injected.flatMap { scope ->
            scope.__generated_scope.features
        }

        val features = elements.map { element ->
            val feature = allFeatures.find { feature ->
                feature.element == element
            }

            FeatureElementPair(
                feature = feature,
                element = element
            )
        }

        val featuresFlows = features.map { featureElementPair ->
            val (feature, element) = featureElementPair

            feature?.feature?.asFlow()?.map { state ->
                StateFeaturePair(
                    state = state,
                    feature = feature,
                    element = element
                )
            } ?: flowOf(
                StateFeaturePair(
                    state = null,
                    feature = null,
                    element = element
                )
            )
        }.toTypedArray()

        return@flatMapLatest if (featuresFlows.isEmpty()) {
            flowOf(emptyList())
        } else {
            combine(*featuresFlows) { flows ->
                flows.map { stateFeaturePair ->
                    FeatureModel(
                        isKnown = (stateFeaturePair.feature != null),
                        element = stateFeaturePair.element,
                        state = stateFeaturePair.state,
                        toggleable = stateFeaturePair.feature?.toggleable,
                        description = stateFeaturePair.feature?.description,
                    )
                }
            }
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = modelsFlow

    private data class FeatureElementPair(
        val feature: InteractiveFeatureData?,
        val element: ElementData,
    )

    private data class StateFeaturePair(
        val feature: InteractiveFeatureData?,
        val state: Feature.State<*>?,
        val element: ElementData,
    )
}