package game.views

import engine.Engine
import engine.text.centerPad
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Functions.fromConsumer
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.view.base.BaseView
import java.util.function.Consumer
import kotlin.math.max

class StartView(engine: Engine, settingsView: SettingsView)
    : BaseView(engine.tileGrid(), engine.colorTheme()) {
    private var btnLabels: HashMap<String, String> = HashMap();

    init {
        val title = "Sandbox"
        btnLabels["start"] = "START!"
        btnLabels["settings"] = "SETTINGS"
        btnLabels["exit"] = "EXIT"

        val btnWidth = 10
        for (entry in btnLabels) {
            entry.setValue(centerPad(entry.value, btnWidth))
        }

        val contentWidth = max(title.length, 20)
        val menuPadding = 1;

        val header = Components.header()
            .withText(title)
            .withPosition(engine.tileGrid().width/2 - title.length/2, 15)
            .build()

        // 9 = size of 3 buttons height
        val menuHeight = header.height + menuPadding + 9
        val menuWidth = contentWidth + menuPadding*2

        val menu = Components.vbox()
            .withSize(menuWidth, menuHeight)
            .withPosition(engine.tileGrid().width/2 - menuWidth/2, header.y + header.height + menuPadding)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build()

        val btnStart = Components.button()
            .withText(btnLabels["start"]!!)
            .withSize(btnWidth+2, 3)
            .withAlignmentWithin(menu, ComponentAlignment.TOP_CENTER)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build()

        val btnSettings = Components.button()
            .withText(btnLabels["settings"]!!)
            .withSize(btnWidth+2, 3)
            .withAlignmentWithin(menu, ComponentAlignment.TOP_CENTER)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build()

        btnSettings.processComponentEvents(ComponentEventType.ACTIVATED, fromConsumer(Consumer {
            engine.dockView(settingsView)
        }))

        val btnExit = Components.button()
            .withText(btnLabels["exit"]!!)
            .withSize(btnWidth+2, 3)
            .withAlignmentWithin(menu, ComponentAlignment.TOP_CENTER)
            .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
            .build()

        btnExit.processComponentEvents(ComponentEventType.ACTIVATED, fromConsumer(Consumer {
            engine.shutdown()
        }))

        menu.addComponents(btnStart, btnSettings, btnExit)
        screen.addComponents(header, menu)
    }
}