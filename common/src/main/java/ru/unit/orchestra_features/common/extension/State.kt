package ru.unit.orchestra_features.common.extension

import ru.unit.orchestra_features.common.support.Feature

fun <T> Feature<T>.state(block: (isToggled: Boolean, data: T) -> Unit) {
    block.invoke(state.isToggled, state.data)
}