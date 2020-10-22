package game

import engine.Engine
import engine.math.Int2
import game.views.SettingsView
import game.views.StartView

fun main(args: Array<String>) {
    val engine = Engine(Int2(80, 45), Int2(1280, 720), "Sandbox")
    val settingsView = SettingsView(engine)
    val startView = StartView(engine, settingsView)

    engine.dockView(startView)
}