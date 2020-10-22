package game

import engine.Engine
import engine.math.Int2
import game.views.SettingsView
import game.views.StartView
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.SwingApplications.startTileGrid
import org.hexworks.zircon.api.TrueTypeFontResources
import org.hexworks.zircon.api.application.AppConfig
import java.awt.Toolkit

fun main(args: Array<String>) {
    val engine = Engine(Int2(80, 45), Int2(1280, 720), "Sandbox")
    val settingsView = SettingsView(engine)
    val startView = StartView(engine, settingsView)

    engine.dockView(startView)
}