package engine

import engine.math.Int2
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.TrueTypeFontResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.resource.TilesetResource
import org.hexworks.zircon.api.view.base.BaseView
import java.awt.Toolkit
import kotlin.system.exitProcess

class Engine(
    private var gridSize: Int2,
    private var resolution: Int2,
    private var title: String = "Untitled",
    private var colorTheme: ColorTheme = ColorThemes.pabloNeruda(),
    private var fullscreen: Boolean = false
) {
    private var tileGrid: TileGrid
    private var scrSize: Int2 = Int2(Toolkit.getDefaultToolkit().screenSize)
    private var tileSize: Int = 0

    private var view: BaseView? = null
    private var previosView: BaseView? = null

    private var tileset: TilesetResource

    init {
        tileSize = if (fullscreen) {
            scrSize.y / gridSize.y
        } else {
            resolution.y / gridSize.y
        }
        tileset = TrueTypeFontResources.ibmBios(tileSize)
        this.tileGrid = SwingApplications.startTileGrid(
            AppConfig.newBuilder()
                .withTitle(title)
                .withSize(gridSize.toSize())
                .withDefaultTileset(tileset)
                .build()
        )
    }

    fun width() : Int {
        return this.gridSize.x
    }

    fun height() : Int {
        return this.gridSize.y
    }

    fun tileGrid() : TileGrid {
        return this.tileGrid
    }

    fun colorTheme() : ColorTheme {
        return this.colorTheme
    }

    fun dockView(view: BaseView) {
        if (this.view == null) {
            this.view = view
            this.view!!.dock()
        } else {
            this.previosView = this.view
            this.view!!.replaceWith(view)
        }
    }

    fun dockPreviousView() {
        if (previosView == null) { return }

        val tmp = view!!
        view!!.replaceWith(previosView!!)
        view = previosView
        previosView = tmp
    }

    fun getView() : BaseView? {
        return this.view
    }

    fun shutdown() {
        exitProcess(0)
    }
}
