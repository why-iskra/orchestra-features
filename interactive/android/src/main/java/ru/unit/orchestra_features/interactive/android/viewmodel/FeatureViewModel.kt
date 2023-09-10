package ru.unit.orchestra_features.interactive.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.common.extension.asFlow
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData
import ru.unit.orchestra_features.interactive.android.AndroidFeatureScopeInjector

class FeatureViewModel(
    private val id: String
) : ViewModel() {

    private val _statusBarStateFlow = MutableStateFlow<StatusBarState>(StatusBarState.Light)
    val statusBarStateFlow = _statusBarStateFlow.asStateFlow()

    private val dataFlow = MutableStateFlow<FeatureScopeData?>(null)

    val featureNameFlow = dataFlow.filterNotNull().map { value ->
        "${value.scope.element.name} ${value.feature.element.name}"
    }

    val featureDescriptionFlow = dataFlow.filterNotNull().map { value ->
        value.feature.description
    }

    @OptIn(FlowPreview::class)
    val featureStateFlow = dataFlow.filterNotNull().flatMapConcat { value ->
        value.feature.feature.asFlow()
    }

    val dependsOnFlow by FeatureModelsProducer(
        dataFlow.filterNotNull().map { featureScopeData ->
            featureScopeData.feature.dependsOn
        }
    )

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
            val data = AndroidFeatureScopeInjector.injected.flatMap { scope ->
                scope.__generated_scope.features.map { feature ->
                    FeatureScopeData(
                        feature = feature,
                        scope = scope.__generated_scope
                    )
                }
            }.find { data ->
                data.feature.element.id == id
            }

            dataFlow.emit(data)
        }
    }

    private data class FeatureScopeData(
        val scope: InteractiveScopeData,
        val feature: InteractiveFeatureData
    )
}