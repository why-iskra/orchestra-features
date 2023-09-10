package ru.unit.orchestra_features.interactive.android

import ru.unit.orchestra_features.common.support.interactive.InteractiveOrchestra

object AndroidOrchestraInjector {

    private val orchestraList = mutableListOf<InteractiveOrchestra>()
    internal val injected: List<InteractiveOrchestra> = orchestraList

    fun inject(orchestra: InteractiveOrchestra) {
        orchestraList.add(orchestra)
    }

    fun remove(orchestra: InteractiveOrchestra) {
        orchestraList.remove(orchestra)
    }
}