package engine.managers

import engine.RESOLUTIONS
import engine.Resolution
import engine.utils.SingletonHolder
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.component.ColorTheme
import org.hexworks.zircon.internal.resource.ColorThemeResource
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private val DefaultSettings = Settings(
    RESOLUTIONS.keys.elementAt(1),
    false, "PABLO_NERUDA")

private fun themeFromName(name: String) : ColorTheme {
    for (theme in ColorThemeResource.values()) {
        if (theme.name == name) {
            return theme.getTheme()
        }
    }
    return ColorThemes.pabloNeruda()
}

private fun themeToName(colorTheme: ColorTheme) : String {
    for (theme in ColorThemeResource.values()) {
        val colTheme = theme.getTheme()
        if (colTheme == colorTheme) {
            return theme.name
        }
    }
    return "DEFAULT"
}

data class Settings(var resolution: Resolution, var fullscreen: Boolean, private var colorThemeName: String) {
    private var colorTheme: ColorTheme = themeFromName(colorThemeName.toUpperCase())

    fun setTheme(colorTheme: ColorTheme) {
        colorThemeName = themeToName(colorTheme)
        this.colorTheme = colorTheme
    }

    fun setTheme(name: String) {
        colorThemeName = name
        colorTheme = themeFromName(name)
    }

    fun getTheme() : ColorTheme {
        return colorTheme
    }

    override fun toString() : String {
        return "resolution=$resolution\nfullscreen=$fullscreen\ntheme=${colorThemeName.toUpperCase()}"
    }

    companion object {
        @JvmStatic
        fun fromFile(filepath: String) : Settings? {
            if (!Files.exists(Paths.get(filepath))) return null

            val lines = File(filepath).readLines()
            if (lines.size != 3) {
                throw RuntimeException("settings file is malformed")
            }

            val resolution = Resolution.fromStr(lines[0])
            val fullscreen = lines[1].split('=')[1].toLowerCase() == "true"
            val theme = lines[2].split('=')[1]

            return Settings(resolution, fullscreen, theme)
        }
    }
}

class SettingsManager private constructor(title: String) {
    private var homePath: Path = Paths.get(System.getProperty("user.home"))
    private var dataPath: Path = Paths.get("$homePath\\$title")
    private var settingsPath: Path = Paths.get("$dataPath\\settings.conf")
    var settings: Settings = DefaultSettings

    init {
        // check if sandbox directory exists in user.home
        if (!Files.exists(dataPath)) {
            // folder doesn't exist, create it
            Files.createDirectory(dataPath)
        }

        // check if settings file exists
        if (!Files.exists(settingsPath)) {
            // settings file doesn't exist, create it
            Files.createFile(settingsPath)

            // write default settings to file
            File(settingsPath.toUri()).printWriter().use {
                it.print("$settings")
            }
        } else {
            // file exists, load settings from file
            settings = Settings.fromFile(settingsPath.toString())!!
        }
    }

    fun save() {
        File(settingsPath.toUri()).printWriter().use {
            it.print("$settings")
        }
    }

    companion object : SingletonHolder<SettingsManager, String>(::SettingsManager)
}