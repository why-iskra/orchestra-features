package ru.unit.orchestra_features.interactive.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.interactive.android.AndroidOrchestraInjector
import ru.unit.orchestra_features.interactive.android.model.FeatureModel
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData

class FeatureViewModel(
    private val id: String
) : ViewModel() {

    private val _statusBarStateFlow = MutableStateFlow<StatusBarState>(StatusBarState.Light)
    val statusBarStateFlow = _statusBarStateFlow.asStateFlow()

    private val dataFlow = MutableStateFlow<FeatureScopeData?>(null)

    val featureNameFlow = dataFlow.filterNotNull().map { value ->
        "${value.module} ${value.scope.element.name} ${value.feature.element.name}"
    }

    val featureDescriptionFlow = dataFlow.filterNotNull().map { value ->
        value.feature.description
    }

    @OptIn(FlowPreview::class)
    val featureStateFlow = dataFlow.filterNotNull().flatMapConcat { value ->
        value.feature.feature.asFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dependsOnFlow = dataFlow.filterNotNull().flatMapLatest { featureScopeData ->
        val allFeatures = AndroidOrchestraInjector.injected.flatMap { orchestra ->
            orchestra.__generated_scopes.flatMap { scope ->
                scope.features
            }
        }

        val features = featureScopeData.feature.dependsOn.map { element ->
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

    init {
        update()
    }

    fun onBottomSheetShown() {
        viewModelScope.launch {
            _statusBarStateFlow.emit(StatusBarState.Dark)
        }
    }

    fun onBottomSheetHidden() {
        viewModelScope.launch {
            _statusBarStateFlow.emit(StatusBarState.Light)
        }
    }

    fun onToggle(toggled: Boolean) {
        viewModelScope.launch {
            dataFlow.value?.feature?.toggleable?.__interactiveToggle(toggled)
        }
    }

    private fun update() {
        viewModelScope.launch {
            val data = AndroidOrchestraInjector.injected.flatMap { orchestra ->
                orchestra.__generated_scopes.flatMap { scope ->
                    scope.features.map { feature ->
                        FeatureScopeData(
                            module = orchestra.__generated_module,
                            feature = feature,
                            scope = scope
                        )
                    }
                }
            }.find { data ->
                data.feature.element.id == id
            }

            dataFlow.emit(data)
        }
    }

    private data class FeatureScopeData(
        val module: String,
        val scope: InteractiveScopeData,
        val feature: InteractiveFeatureData
    )

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