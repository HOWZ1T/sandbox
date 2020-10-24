package game

import engine.Engine
import engine.math.Int2
import game.views.PlayView
import game.views.SettingsView
import game.views.StartView

fun main(args: Array<String>) {
    val title = "Sandbox"
    val engine = Engine(Int2(80, 45), title)

    val playView = PlayView(engine)
    val settingsView = SettingsView(engine)
    val startView = StartView(engine, settingsView, playView)

    engine.dockView(startView)
}