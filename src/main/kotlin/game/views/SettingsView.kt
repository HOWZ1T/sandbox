package game.views

import engine.Engine
import engine.text.centerPad
import engine.ui.CheckboxRenderer
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Functions
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.view.base.BaseView
import java.util.function.Consumer

class SettingsView(engine: Engine)
    : BaseView(engine.tileGrid(), engine.colorTheme()) {
    init {
        /* -- SETTINGS PANEL -- */
        val title = "Settings"
        val vboxSettings = Components.vbox()
            .withSpacing(1)
            .withSize(60, 40)
            .withPosition(engine.tileGrid().width/2 - 30, engine.tileGrid().height/2 - 20)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE, title))
            .build()

        /* -- RESOLUTION CONTROL -- */
        val hboxResolution = Components.hbox()
            .withSize(54, 3)
            .withAlignmentWithin(vboxSettings, ComponentAlignment.TOP_CENTER)
            .withPosition(2, 2)
            .build()

        val lblResolution = Components.label().withText("           Resolution: ").build()

        val btnResolutionPrev = Components.button()
            .withText("<").withDecorations(ComponentDecorations.side('<', '<'))
            .build()

        val lblCurResolution = Components.label()
            .withText(centerPad("1920 x 1080", 13, ' '))
            .build()

        val btnResolutionNext = Components.button()
            .withText(">").withDecorations(ComponentDecorations.side('>', '>'))
            .build()
        
        /* -- FULLSCREEN CONTROL -- */
        val hboxFullscreen = Components.hbox()
            .withSize(54, 3)
            .withAlignmentWithin(vboxSettings, ComponentAlignment.TOP_CENTER)
            .withPosition(2, 1)
            .build()

        val lblFullscreen = Components.label().withText("           Fullscreen: ").build()
        val chckBoxFullscreen = Components.checkBox()
            .withText("")
            .withComponentRenderer(CheckboxRenderer())
            .build()

        /* -- THEME CONTROL -- */
        val hboxTheme = Components.hbox()
            .withSize(54, 3)
            .withAlignmentWithin(vboxSettings, ComponentAlignment.TOP_CENTER)
            .build()

        val lblTheme = Components.label().withText("           Theme: ").build()

        val btnThemePrev = Components.button()
            .withText("<").withDecorations(ComponentDecorations.side('<', '<'))
            .build()

        val lblCurTheme = Components.label()
            .withText(centerPad("Solarized Light Magenta", 26))
            .build()

        val btnThemeNext = Components.button()
            .withText(">").withDecorations(ComponentDecorations.side('>', '>'))
            .build()

        /* -- BACK AND APPLY CONTROLS -- */
        val hboxControls = Components.hbox()
            .withSize(54, 3)
            .withAlignmentWithin(vboxSettings, ComponentAlignment.TOP_CENTER)
            .build()

        val btnBack = Components.button()
            .withText(" BACK ")
            .withPosition(11, 0)
            .build()

        val btnApply = Components.button()
            .withText(" APPLY ")
            .withPosition(12, 0)
            .build()

        /* --   ADDING LOGIC    -- */
        btnBack.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            engine.dockPreviousView()
        }))

        /* -- ADDING COMPONENTS -- */
        hboxResolution.addComponents(lblResolution, btnResolutionPrev, lblCurResolution, btnResolutionNext)
        hboxFullscreen.addComponents(lblFullscreen, chckBoxFullscreen)
        hboxTheme.addComponents(lblTheme, btnThemePrev, lblCurTheme, btnThemeNext)
        hboxControls.addComponents(btnBack, btnApply)

        vboxSettings.addComponents(hboxResolution, hboxFullscreen, hboxTheme, hboxControls)
        screen.addComponents(vboxSettings)
    }
}