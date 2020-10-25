package game.views

import engine.Engine
import engine.Resolution
import engine.text.centerPad
import engine.ui.CheckboxRenderer
import engine.managers.SettingsManager
import engine.ui.BooleanModalResult
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Functions
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.component.impl.DefaultCheckBox

class SettingsView(engine: Engine) : BaseView(engine.tileGrid(), engine.colorTheme()) {
    private var settingsManager: SettingsManager = SettingsManager.getInstance(engine.title())
    private var colorTheme: ColorTheme
    private var resolution: Resolution

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

        btnResolutionNext.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            resolution = engine.nextResolution(resolution)
            lblCurResolution.text = centerPad(resolution.sizeAsStr(), 13)
        })

        btnResolutionPrev.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            resolution = engine.prevResolution(resolution)
            lblCurResolution.text = centerPad(resolution.sizeAsStr(), 13)
        })

        btnThemeNext.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            colorTheme = engine.nextColorTheme(colorTheme)
            lblCurTheme.text = centerPad(engine.colorThemeName(colorTheme), 26)
            screen.theme = colorTheme
        })

        btnThemePrev.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            colorTheme = engine.prevColorTheme(colorTheme)
            lblCurTheme.text = centerPad(engine.colorThemeName(colorTheme), 26)
            screen.theme = colorTheme
        })

        btnBack.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            // have settings been changed ?
            if (theme != settingsManager.settings.getTheme() || resolution != settingsManager.settings.resolution
                || chckBoxFullscreen.isSelected != settingsManager.settings.fullscreen
            ) {
                val modal = createDoApplyModal(screen, theme)
                modal.onClosed {
                    if (it.accepted()) {
                        engine.setColorTheme(theme)
                        settingsManager.settings.resolution = resolution
                        settingsManager.settings.fullscreen = chckBoxFullscreen.isSelected

                        val modalRestart = createRestartRequiredModal(screen, theme)
                        modalRestart.onClosed { jt ->
                            if (jt.accepted()) {
                                engine.shutdown()
                            } else {
                                engine.dockPreviousView()
                            }
                        }
                        screen.openModal(modalRestart)
                    } else {
                        screen.theme = settingsManager.settings.getTheme()
                        lblCurResolution.text = centerPad(settingsManager.settings.resolution.sizeAsStr(), 13)
                        lblCurTheme.text = centerPad(engine.colorThemeName(settingsManager.settings.getTheme()), 26)
                        chckBoxFullscreen.isSelected = settingsManager.settings.fullscreen
                        engine.dockPreviousView()
                    }
                }
                screen.openModal(modal)
            } else {
                screen.theme = settingsManager.settings.getTheme()
                lblCurResolution.text = centerPad(settingsManager.settings.resolution.sizeAsStr(), 13)
                lblCurTheme.text = centerPad(engine.colorThemeName(settingsManager.settings.getTheme()), 26)
                chckBoxFullscreen.isSelected = settingsManager.settings.fullscreen
                engine.dockPreviousView()
            }
        })

        btnApply.processComponentEvents(ComponentEventType.ACTIVATED, Functions.fromConsumer {
            // have settings been changed ?
            if (theme != settingsManager.settings.getTheme() || resolution != settingsManager.settings.resolution
                || chckBoxFullscreen.isSelected != settingsManager.settings.fullscreen
            ) {
                engine.setColorTheme(colorTheme) // sets theme in to settings too
                settingsManager.settings.fullscreen =
                    chckBoxFullscreen.checkBoxState == DefaultCheckBox.CheckBoxState.CHECKED
                settingsManager.settings.resolution = resolution
                settingsManager.save()

                val modal = createRestartRequiredModal(screen, colorTheme)
                modal.onClosed {
                    if (it.accepted()) {
                        engine.shutdown()
                    } else {
                        engine.dockPreviousView()
                    }
                }
                screen.openModal(modal)
            } else {
                engine.dockPreviousView()
            }
        })
        
        /* -- ADDING COMPONENTS -- */
        hboxResolution.addComponents(lblResolution, btnResolutionPrev, lblCurResolution, btnResolutionNext)
        hboxFullscreen.addComponents(lblFullscreen, chckBoxFullscreen)
        hboxTheme.addComponents(lblTheme, btnThemePrev, lblCurTheme, btnThemeNext)
        hboxControls.addComponents(btnBack, btnApply)

        vboxSettings.addComponents(hboxResolution, hboxFullscreen, hboxTheme, hboxControls)
        screen.addComponents(vboxSettings)
    }

    private fun addYesNoButtons(modal: Modal<BooleanModalResult>, panel: Panel) {
        val btnNo = Components.button()
            .withText(" NO ")
            .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
            .build().apply {
                handleComponentEvents(ComponentEventType.ACTIVATED) {
                    modal.close(BooleanModalResult(false))
                    Processed
                }
            }
        btnNo.moveRightBy(16)

        val btnYes = Components.button()
            .withText(" YES ")
            .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_RIGHT)
            .build().apply {
                handleComponentEvents(ComponentEventType.ACTIVATED) {
                    modal.close(BooleanModalResult(true))
                    Processed
                }
            }
        btnYes.moveLeftBy(1)

        panel.addComponents(btnNo, btnYes)
    }

    private fun createDoApplyModal(screen: Screen, colorTheme: ColorTheme) : Modal<BooleanModalResult> {
        val panel = Components.panel()
            .withSize(Size.create(34, 8))
            .withDecorations(ComponentDecorations.box(), ComponentDecorations.shadow())
            .build()

        val modal = ModalBuilder.newBuilder<BooleanModalResult>()
            .withComponent(panel)
            .withParentSize(screen.size)
            .withColorTheme(colorTheme)
            .build()

        val paraMsg = Components.paragraph()
            .withText("Do you want to apply changes?")
            .withPosition(1, 1)
            .withSize(29, 3)
            .build()

        panel.addComponent(paraMsg)
        addYesNoButtons(modal, panel)
        return modal
    }

    private fun createRestartRequiredModal(screen: Screen, colorTheme: ColorTheme) : Modal<BooleanModalResult> {
        val panel = Components.panel()
            .withSize(Size.create(34, 13))
            .withDecorations(ComponentDecorations.box(), ComponentDecorations.shadow())
            .build()

        val modal = ModalBuilder.newBuilder<BooleanModalResult>()
            .withComponent(panel)
            .withParentSize(screen.size)
            .withColorTheme(colorTheme)
            .build()

        val spaces = " ".repeat(29)
        val paraMsg = Components.paragraph()
            .withText("Restart Required for changes to take effect.              " + spaces +
                    "Do you want to quit now?")
            .withPosition(1, 1)
            .withSize(29, 6)
            .build()

        panel.addComponent(paraMsg)
        addYesNoButtons(modal, panel)
        return modal
    }
}