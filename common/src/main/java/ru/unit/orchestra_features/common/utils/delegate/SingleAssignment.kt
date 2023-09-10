package ru.unit.orchestra_features.common.utils.delegate

import kotlin.reflect.KProperty

class SingleAssignment<T : Any>(
    private val causeException: Boolean = true
) {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        this.value ?: throw UninitializedPropertyAccessException()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (this.value != null && causeException) {
            throw SingleAssignmentException()
        }

        this.value = value
    }

    fun controller(): Controller = ControllerImpl()

    interface Controller {

        fun reset()
    }

    inner class ControllerImpl : Controller {

        override fun reset() {
            value = null
        }
    }

    class GroupController(
        controllers: List<Controller> = emptyList()
    ) : Controller {

        private val controllers = controllers.toMutableList()

        fun add(controller: Controller) {
            controllers.add(controller)
        }

        fun remove(controller: Controller) =
            controllers.remove(controller)

        fun contains(controller: Controller) =
            controllers.contains(controller)

        override fun reset() {
            controllers.forEach(Controller::reset)
        }
    }

    class SingleAssignmentException : RuntimeException()
}