package ru.unit.orchestra_features.interactive.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.interactive.android.AndroidOrchestraInjector
import ru.unit.orchestra_features.interactive.android.model.FeatureModel
import ru.unit.orchestra_features.interactive.android.model.ScopeModel
import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.common.support.interactive.InteractiveOrchestra
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData

class MainViewModel : ViewModel() {

    private val _statusBarStateFlow = MutableStateFlow<StatusBarState>(StatusBarState.Light)
    val statusBarStateFlow = _statusBarStateFlow.asStateFlow()

    private val _scopeModelsFlow = MutableStateFlow<List<OrchestraScopePair>>(emptyList())
    val scopeModelsFlow = _scopeModelsFlow.map { orchestraScopes ->
        orchestraScopes.map { orchestraScope ->
            ScopeModel(
                element = orchestraScope.scope.element,
                module = orchestraScope.orchestra.__generated_module
            )
        }
    }

    private val currentScopeFlow = MutableStateFlow<ElementData?>(null)

    private val scopeFlow = _scopeModelsFlow.combine(currentScopeFlow) { orchestraScopes, element ->
        orchestraScopes.find { orchestraScope ->
            orchestraScope.scope.element == element
        } ?: orchestraScopes.firstOrNull()
    }

    val scopeNameFlow = scopeFlow.filterNotNull().map { data -> data.scope.element.name }

    @OptIn(ExperimentalCoroutinesApi::class)
    val featureModelsFlow = scopeFlow.filterNotNull().flatMapLatest { orchestraScope ->
        val features = orchestraScope.scope.features

        val featuresFlows = features.map { feature ->
            feature.feature.asFlow().map { state ->
                StateFeaturePair(
                    state = state,
                    feature = feature
                )
            }
        }.toTypedArray()

        combine(*featuresFlows) { flows ->
            flows.map { stateFeaturePair ->
                FeatureModel(
                    element = stateFeaturePair.feature.element,
                    state = stateFeaturePair.state,
                    toggleable = stateFeaturePair.feature.toggleable,
                    description = stateFeaturePair.feature.description,
                    isKnown = true
                )
            }
        }
    }

    init {
        update()
    }

    fun onScopeChosen(scopeElement: ElementData) {
        viewModelScope.launch {
            currentScopeFlow.emit(scopeElement)
        }
    }

    fun onSearchShown() {
        viewModelScope.launch {
            _statusBarStateFlow.emit(StatusBarState.Dark)
        }
    }

    fun onSearchHidden() {
        viewModelScope.launch {
            _statusBarStateFlow.emit(StatusBarState.Light)
        }
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

    private fun update() {
        viewModelScope.launch {
            val scopes = AndroidOrchestraInjector.injected.flatMap { orchestra ->
                orchestra.__generated_scopes.map { scope ->
                    OrchestraScopePair(
                        orchestra = orchestra,
                        scope = scope
                    )
                }.sortedBy { orchestraScope ->
                    orchestraScope.orchestra.__generated_module + orchestraScope.scope.element.name
                }
            }

            _scopeModelsFlow.emit(scopes)
        }
    }

    private data class OrchestraScopePair(
        val scope: InteractiveScopeData,
        val orchestra: InteractiveOrchestra
    )

    private data class StateFeaturePair(
        val feature: InteractiveFeatureData,
        val state: Feature.State<*>,
    )
}