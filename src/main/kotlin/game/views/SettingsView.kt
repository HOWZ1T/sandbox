package game.views

import engine.Engine
import engine.Resolution
import engine.text.centerPad
import engine.ui.CheckboxRenderer
import engine.managers.SettingsManager
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Functions
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.component.impl.DefaultCheckBox
import java.util.function.Consumer

class SettingsView(engine: Engine) : BaseView(engine.tileGrid(), engine.colorTheme()) {
    private var settingsManager: SettingsManager = SettingsManager.getInstance(engine.title())
    private lateinit var colorTheme: ColorTheme;
    private lateinit var resolution: Resolution;

    init {
        colorTheme = settingsManager.settings.getTheme()
        resolution = settingsManager.settings.resolution

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
            .withText(centerPad(resolution.sizeAsStr(), 13, ' '))
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

        chckBoxFullscreen.isSelected = settingsManager.settings.fullscreen

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
            .withText(centerPad(engine.colorThemeName(colorTheme), 26))
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

        btnResolutionNext.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            resolution = engine.nextResolution(resolution)
            lblCurResolution.text = centerPad(resolution.sizeAsStr(), 13)
        }))

        btnResolutionPrev.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            resolution = engine.prevResolution(resolution)
            lblCurResolution.text = centerPad(resolution.sizeAsStr(), 13)
        }))

        btnThemeNext.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            colorTheme = engine.nextColorTheme(colorTheme)
            lblCurTheme.text = centerPad(engine.colorThemeName(colorTheme), 26)
            screen.theme = colorTheme
        }))

        btnThemePrev.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            colorTheme = engine.prevColorTheme(colorTheme)
            lblCurTheme.text = centerPad(engine.colorThemeName(colorTheme), 26)
            screen.theme = colorTheme
        }))

        btnBack.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            screen.theme = settingsManager.settings.getTheme()
            engine.dockPreviousView()
            // TODO if settings changed, prompt user if they want to apply it with modal

            // TODO: IF NOT SAVING
            lblCurResolution.text = centerPad(settingsManager.settings.resolution.sizeAsStr(), 13)
            lblCurTheme.text = centerPad(engine.colorThemeName(settingsManager.settings.getTheme()), 26)
            chckBoxFullscreen.isSelected = settingsManager.settings.fullscreen
        }))

        btnApply.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer(Consumer {
            engine.setColorTheme(colorTheme) // sets theme in to settings too
            settingsManager.settings.fullscreen = chckBoxFullscreen.checkBoxState == DefaultCheckBox.CheckBoxState.CHECKED
            settingsManager.settings.resolution = resolution
            // TODO prompt user to restart with modal
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