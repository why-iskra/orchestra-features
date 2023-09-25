package ru.unit.orchestra_features.common.extension

import ru.unit.orchestra_features.common.support.Feature
import ru.unit.orchestra_features.common.support.utils.Otherwise

fun <T> Feature<T>.ifToggled(block: (data: T) -> Unit): Otherwise {
    val isToggled = state.isToggled

    if (isToggled) {
        block.invoke(state.data)
    }

    return Otherwise(!isToggled)
}

fun <T, R> Feature<T>.ifToggled(
    ifBlock: (data: T) -> R,
    elseBlock: () -> R
) = if (state.isToggled) {
    ifBlock.invoke(state.data)
} else {
    elseBlock.invoke()
}