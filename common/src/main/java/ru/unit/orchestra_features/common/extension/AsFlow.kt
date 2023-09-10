package ru.unit.orchestra_features.common.extension

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import ru.unit.orchestra_features.common.support.Feature

/**
 * Convert feature to kotlin's flow
 *
 * @param emitLastValueImmediately if true, current state will be emitted first
 * @param ignoreDataChanges if true, the data changes will be ignored as an event for emit
 */
fun <T> Feature<T>.asFlow(
    emitLastValueImmediately: Boolean = true,
    ignoreDataChanges: Boolean = false,
) = callbackFlow {
    var lastValue = state

    if (emitLastValueImmediately) {
        trySend(lastValue)
    }

    val listener = Feature.Listener { state ->
        if (ignoreDataChanges) {
            if (lastValue.isToggled == state.isToggled) {
                return@Listener
            }
        } else {
            if (lastValue == state) {
                return@Listener
            }
        }

        lastValue = state

        trySend(state)
    }

    addListener(listener)

    awaitClose {
        removeListener(listener)
    }
}
