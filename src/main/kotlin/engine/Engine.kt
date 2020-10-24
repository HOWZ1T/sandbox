package engine

import engine.math.Int2
import engine.managers.SettingsManager
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.TrueTypeFontResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.resource.TilesetResource
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.resource.ColorThemeResource
import java.awt.Toolkit
import kotlin.system.exitProcess

data class Resolution(val size: Int2, val aspectRatio: Int2) {
    fun sizeAsStr() : String {
        return "${size.x} x ${size.y}"
    }

    override fun toString() : String {
        return "Resolution(size=$size, aspectRatio=$aspectRatio)"
    }

    companion object {
        @JvmStatic
        fun fromStr(str: String) : Resolution {
            // format should be : Resolution(size=Int2(x, y), aspectRatio=Int2(x, y))
            val rawParts = str.split("Int2")

            val rawResParts = rawParts[1].split(", aspect")[0]
            val rawRes = rawResParts.substring(1, rawResParts.length-1).split(',')

            var rawAspectParts = rawParts[2]
            val rawAspect = rawAspectParts.substring(1, rawAspectParts.length-2).split(',')

            return Resolution(Int2(Integer.valueOf(rawRes[0].trim()), Integer.valueOf(rawRes[1].trim())),
                              Int2(Integer.valueOf(rawAspect[0].trim()), Integer.valueOf(rawAspect[1].trim())))

        }
    }
}

val RESOLUTIONS: Map<Resolution, Int> = mapOf(
    Resolution(Int2(640, 360), Int2(16, 9))   to 0,
    Resolution(Int2(1280, 720), Int2(16, 9))  to 1,
    Resolution(Int2(1600, 900), Int2(16, 9))  to 2,
    Resolution(Int2(1920, 1080), Int2(16, 9)) to 3,
    Resolution(Int2(2560, 1440), Int2(16, 9)) to 4,
    Resolution(Int2(2880, 1620), Int2(16, 9)) to 5,
    Resolution(Int2(3200, 1800), Int2(16, 9)) to 6,
    Resolution(Int2(3840, 2160), Int2(16, 9)) to 7
)

val THEMES: Map<ColorTheme, Pair<String, Int>> = mapOf(
    ColorThemeResource.ADRIFT_IN_DREAMS.getTheme() to Pair("Adrift In Dreams", 0),
    ColorThemeResource.AFTERGLOW.getTheme() to Pair("After Glow", 1),
    ColorThemeResource.AFTER_THE_HEIST.getTheme() to Pair("After The Heist", 2),
    ColorThemeResource.AMIGA_OS.getTheme() to Pair("Amiga OS", 3),
    ColorThemeResource.ANCESTRY.getTheme() to Pair("Ancestry", 4),
    ColorThemeResource.ARC.getTheme() to Pair("Arc", 5),
    ColorThemeResource.CAPTURED_BY_PIRATES.getTheme() to Pair("Captured By Pirates", 6),
    ColorThemeResource.CYBERPUNK.getTheme() to Pair("Cyberpunk", 7),
    ColorThemeResource.DEFAULT.getTheme() to Pair("Default", 8),
    ColorThemeResource.ENTRAPPED_IN_A_PALETTE.getTheme() to Pair("Entrapped In A Palette", 9),
    ColorThemeResource.FOREST.getTheme() to Pair("Forest", 10),
    ColorThemeResource.GAMEBOOKERS.getTheme() to Pair("Gamebookers", 11),
    ColorThemeResource.GHOST_OF_A_CHANCE.getTheme() to Pair("Ghost Of A Chance", 12),
    ColorThemeResource.HEADACHE.getTheme() to Pair("Headache", 13),
    ColorThemeResource.INGRESS_ENLIGHTENED.getTheme() to Pair("Ingress Enlightened", 14),
    ColorThemeResource.INGRESS_RESISTANCE.getTheme() to Pair("Ingress Resistance", 15),
    ColorThemeResource.LET_THEM_EAT_CAKE.getTheme() to Pair("Let Them Eat Cake", 16),
    ColorThemeResource.LINUX_MINT_DARK.getTheme() to Pair("Linux Mint Dark", 17),
    ColorThemeResource.MONOKAI_BLUE.getTheme() to Pair("Monokai Blue", 18),
    ColorThemeResource.MONOKAI_GREEN.getTheme() to Pair("Monokai Green", 19),
    ColorThemeResource.MONOKAI_ORANGE.getTheme() to Pair("Monokai Orange", 20),
    ColorThemeResource.MONOKAI_PINK.getTheme() to Pair("Monokai Pink", 21),
    ColorThemeResource.MONOKAI_VIOLET.getTheme() to Pair("Monokai Violet", 22),
    ColorThemeResource.MONOKAI_YELLOW.getTheme() to Pair("Monokai Yellow", 23),
    ColorThemeResource.NORD.getTheme() to Pair("Nord", 24),
    ColorThemeResource.OLIVE_LEAF_TEA.getTheme() to Pair("Olive Leaf Tea", 25),
    ColorThemeResource.PABLO_NERUDA.getTheme() to Pair("Pablo Neruda", 26),
    ColorThemeResource.SAIKU.getTheme() to Pair("Saiku", 27),
    ColorThemeResource.SOLARIZED_DARK_BLUE.getTheme() to Pair("Solarized Dark Blue", 28),
    ColorThemeResource.SOLARIZED_DARK_CYAN.getTheme() to Pair("Solarized Dark Cyan", 29),
    ColorThemeResource.SOLARIZED_DARK_GREEN.getTheme() to Pair("Solarized Dark Green", 30),
    ColorThemeResource.SOLARIZED_DARK_MAGENTA.getTheme() to Pair("Solarized Dark Magenta", 31),
    ColorThemeResource.SOLARIZED_DARK_ORANGE.getTheme() to Pair("Solarized Dark Orange", 32),
    ColorThemeResource.SOLARIZED_DARK_RED.getTheme() to Pair("Solarized Dark Red", 33),
    ColorThemeResource.SOLARIZED_DARK_VIOLET.getTheme() to Pair("Solarized Dark Violet", 34),
    ColorThemeResource.SOLARIZED_DARK_YELLOW.getTheme() to Pair("Solarized Dark Yellow", 35),
    ColorThemeResource.SOLARIZED_LIGHT_BLUE.getTheme() to Pair("Solarized Light Blue", 36),
    ColorThemeResource.SOLARIZED_LIGHT_CYAN.getTheme() to Pair("Solarized Light Cyan", 37),
    ColorThemeResource.SOLARIZED_LIGHT_GREEN.getTheme() to Pair("Solarized Light Green", 38),
    ColorThemeResource.SOLARIZED_LIGHT_MAGENTA.getTheme() to Pair("Solarized Light Magenta", 39),
    ColorThemeResource.SOLARIZED_LIGHT_ORANGE.getTheme() to Pair("Solarized Light Orange", 40),
    ColorThemeResource.SOLARIZED_LIGHT_RED.getTheme() to Pair("Solarized Light Red", 41),
    ColorThemeResource.SOLARIZED_LIGHT_VIOLET.getTheme() to Pair("Solarized Light Violet", 42),
    ColorThemeResource.SOLARIZED_LIGHT_YELLOW.getTheme() to Pair("Solarized Light Yellow", 43),
    ColorThemeResource.TECH_LIGHT.getTheme() to Pair("Tech Light", 44),
    ColorThemeResource.TRON.getTheme() to Pair("Tron", 45),
    ColorThemeResource.WAR.getTheme() to Pair("War", 46),
    ColorThemeResource.ZENBURN_GREEN.getTheme() to Pair("Zenburn Green", 47),
    ColorThemeResource.ZENBURN_PINK.getTheme() to Pair("Zenburn Pink", 48),
    ColorThemeResource.ZENBURN_VANILLA.getTheme() to Pair("Zenburn Vanilla", 49)
)

class Engine(
    private var gridSize: Int2,
    private var title: String = "Untitled"
) {
    private var tileGrid: TileGrid
    private var scrSize: Int2 = Int2(Toolkit.getDefaultToolkit().screenSize)
    private var tileSize: Int = 0
    private var settingsManager = SettingsManager.getInstance(title)

    private var view: BaseView? = null
    private var previousView: BaseView? = null

    private var tileset: TilesetResource

    init {
        tileSize = if (settingsManager.settings.fullscreen) {
            scrSize.y / gridSize.y
        } else {
            settingsManager.settings.resolution.size.y / gridSize.y
        }
        tileset = TrueTypeFontResources.ibmBios(tileSize)

        val confBuilder = AppConfig.newBuilder()
            .withTitle(title)
            .withSize(gridSize.toSize())
            .withDefaultTileset(tileset)

        if (settingsManager.settings.fullscreen) {
            confBuilder.fullScreen()
        }

        tileGrid = SwingApplications.startTileGrid(confBuilder.build())
        setColorTheme(settingsManager.settings.getTheme())
    }

    fun width() : Int {
        return this.gridSize.x
    }

    fun height() : Int {
        return this.gridSize.y
    }

    fun title(): String {
        return this.title
    }

    fun tileGrid() : TileGrid {
        return this.tileGrid
    }

    fun colorTheme() : ColorTheme {
        return settingsManager.settings.getTheme()
    }

    fun setColorTheme(colorTheme: ColorTheme) {
        settingsManager.settings.setTheme(colorTheme)
        getView()?.screen?.theme = settingsManager.settings.getTheme()
    }

    fun dockView(view: BaseView) {
        if (this.view == null) {
            this.view = view
            this.view!!.dock()
        } else {
            this.previousView = this.view
            this.view!!.replaceWith(view)
        }
    }

    fun dockPreviousView() {
        if (previousView == null) { return }

        val tmp = view!!
        view!!.replaceWith(previousView!!)
        view = previousView
        previousView = tmp
    }

    fun getView() : BaseView? {
        return view
    }

    fun shutdown() {
        settingsManager.save()
        exitProcess(0)
    }

    fun nextResolution(res: Resolution) : Resolution {
        val iNext = ((RESOLUTIONS[res] ?: error("could not find resolution")) + 1) % RESOLUTIONS.size
        val nextRes = RESOLUTIONS.keys.elementAt(iNext)
        return if (nextRes.size > scrSize) RESOLUTIONS.keys.elementAt(0) else nextRes
    }

    fun prevResolution(res: Resolution) : Resolution {
        var i = (RESOLUTIONS[res] ?: error("could not find resolution")) - 1
        if (i < 0) i = RESOLUTIONS.size - 1
        else i %= RESOLUTIONS.size
        var nextRes = RESOLUTIONS.keys.elementAt(i)
        while (nextRes.size > scrSize) {
           nextRes = RESOLUTIONS.keys.elementAt((--i) % RESOLUTIONS.size)
            println("next: ${nextRes.size} vs scr: ${scrSize}")
        }
        return nextRes
    }

    fun colorThemeName(colTheme: ColorTheme) : String {
        return (THEMES[colTheme] ?: error("color theme not found")).first
    }

    fun nextColorTheme(colTheme: ColorTheme) : ColorTheme {
        val iNext = ((THEMES[colTheme] ?: error("color theme not found")).second + 1) % THEMES.size
        return THEMES.keys.elementAt(iNext)
    }

    fun prevColorTheme(colTheme: ColorTheme) : ColorTheme {
        var iPrev = (THEMES[colTheme] ?: error("color theme not found")).second - 1
        if (iPrev < 0) iPrev = THEMES.size-1
        else iPrev %= THEMES.size
        return THEMES.keys.elementAt(iPrev)
    }
}
