package ru.unit.orchestra_features.interactive.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveScopeData
import ru.unit.orchestra_features.interactive.android.AndroidFeatureScopeInjector
import ru.unit.orchestra_features.interactive.android.model.ScopeModel

class MainViewModel : ViewModel() {

    private val _statusBarStateFlow = MutableStateFlow<StatusBarState>(StatusBarState.Light)
    val statusBarStateFlow = _statusBarStateFlow.asStateFlow()

    private val _scopeModelsFlow = MutableStateFlow<List<InteractiveScopeData>>(emptyList())
    val scopeModelsFlow = _scopeModelsFlow.map { orchestraScopes ->
        orchestraScopes.map { scope ->
            ScopeModel(
                element = scope.element,
            )
        }
    }

    private val currentScopeFlow = MutableStateFlow<ElementData?>(null)

    private val searchQueryFlow = MutableStateFlow<String?>(null)

    private val scopeFlow = _scopeModelsFlow.combine(currentScopeFlow) { orchestraScopes, element ->
        orchestraScopes.find { scope ->
            scope.element == element
        } ?: orchestraScopes.firstOrNull()
    }

    val scopeNameFlow = scopeFlow.filterNotNull().map { data -> data.element.name }

    val featureModelsFlow by FeatureModelsProducer(
        scopeFlow.filterNotNull().map { scope ->
            scope.features.map { feature ->
                feature.element
            }
        }
    )

    val searchFeatureModelsFlow by FeatureModelsProducer(
        scopeFlow.filterNotNull().combine(searchQueryFlow) { scope, query ->
            scope.features.filter { feature ->
                val element = feature.element

                val preparedQuery = (query ?: "").trim().lowercase()

                val queryIsBlank = preparedQuery.isBlank()
                val queryContainsInName = element.name.lowercase().contains(preparedQuery)
                val queryContainsInId = element.id.lowercase().contains(preparedQuery)

                return@filter queryIsBlank || queryContainsInName || queryContainsInId
            }.map { feature ->
                feature.element
            }
        }
    )

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

    fun search(text: String?) {
        viewModelScope.launch {
            searchQueryFlow.emit(text)
        }
    }

    private fun update() {
        viewModelScope.launch {
            val scopes = AndroidFeatureScopeInjector.injected.map { scope ->
                scope.__generated_scope
            }.sortedBy { scope ->
                scope.element.name
            }

            _scopeModelsFlow.emit(scopes)
        }
    }
}