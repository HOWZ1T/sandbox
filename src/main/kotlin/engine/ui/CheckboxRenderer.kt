package engine.ui

import org.hexworks.zircon.api.component.CheckBox
import org.hexworks.zircon.api.component.renderer.ComponentRenderContext
import org.hexworks.zircon.api.component.renderer.ComponentRenderer
import org.hexworks.zircon.api.graphics.TileGraphics
import org.hexworks.zircon.internal.component.impl.DefaultCheckBox

class CheckboxRenderer : ComponentRenderer<CheckBox> {
    override fun render(tileGraphics: TileGraphics, context: ComponentRenderContext<CheckBox>) {
        val state = context.component.checkBoxState
        tileGraphics.fillWithText(
            text = STATES.getValue(state),
            style = context.currentStyle)
    }

    companion object {
        private const val CHECKING_BUTTON = "[+]"
        private const val UNCHECKING_BUTTON = "[-]"
        private const val CHECKED_BUTTON = "[*]"
        private const val UNCHECKED_BUTTON = "[ ]"

        private val STATES = mapOf(
            Pair(DefaultCheckBox.CheckBoxState.UNCHECKED, UNCHECKED_BUTTON),
            Pair(DefaultCheckBox.CheckBoxState.CHECKING, CHECKING_BUTTON),
            Pair(DefaultCheckBox.CheckBoxState.CHECKED, CHECKED_BUTTON),
            Pair(DefaultCheckBox.CheckBoxState.UNCHECKING, UNCHECKING_BUTTON))

    }
}