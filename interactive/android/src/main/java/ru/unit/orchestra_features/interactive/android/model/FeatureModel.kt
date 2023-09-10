package ru.unit.orchestra_features.interactive.android.model

import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.interactive.ElementData
import ru.unit.orchestra_features.common.support.interactive.InteractiveFeatureData

data class FeatureModel(
    val element: ElementData,
    val isKnown: Boolean,
    val description: String?,
    val state: Feature.State<out Any?>?,
    val toggleable: Feature.InteractiveToggleable?,
)