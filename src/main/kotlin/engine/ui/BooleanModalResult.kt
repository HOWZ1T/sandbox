package engine.ui

import org.hexworks.zircon.api.component.modal.ModalResult

class BooleanModalResult(private val accepted: Boolean) : ModalResult {
    fun accepted(): Boolean {
        return accepted
    }

    fun rejected(): Boolean {
        return !accepted
    }
}