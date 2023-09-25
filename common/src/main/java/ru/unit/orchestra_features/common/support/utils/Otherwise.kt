package ru.unit.orchestra_features.common.support.utils

class Otherwise(private val invoke: Boolean) {

    fun otherwise(block: () -> Unit) {
        if (invoke) {
            block.invoke()
        }
    }
}