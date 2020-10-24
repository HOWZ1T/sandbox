package game.views

import engine.Engine
import engine.managers.SettingsManager
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AlignmentStrategy
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.view.base.BaseView

class PlayView(engine: Engine) : BaseView(engine.tileGrid(), engine.colorTheme()) {
    private var settingsManager: SettingsManager = SettingsManager.getInstance(engine.title())

    init {
        val chckBoxFullscreen = Components.checkBox()
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .withText("")
            .build()

        screen.addComponents(chckBoxFullscreen)
    }
}