package ru.unit.orchestra_features.common.extension

import ru.unit.orchestra_features.common.support.Feature

fun anyToggled(vararg features: Feature<*>) =
    features.isNotEmpty() && features.any { feature -> feature.state.isToggled }