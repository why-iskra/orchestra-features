package ru.unit.orchestra_features.common.support

import java.util.Collections

abstract class Feature<T> {

    abstract val state: State<T>

    private val listeners = Collections.synchronizedList(mutableListOf<Listener<T>>())

    protected fun onStateUpdated() {
        listeners.forEach { listener ->
            listener.onUpdated(state)
        }
    }

    internal fun addListener(listener: Listener<T>) {
        listeners.add(listener)
    }

    internal fun removeListener(listener: Listener<T>) {
        listeners.remove(listener)
    }

    internal fun interface Listener<T> {

        fun onUpdated(state: State<T>)
    }

    interface InteractiveToggleable {

        fun __interactiveToggle(value: Boolean)
    }

    data class State<T>(
        val isToggled: Boolean,
        val data: T
    )
}
