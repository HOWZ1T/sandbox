package engine.text

fun centerPad(str: String, targetWidth: Int, padChr: Char = ' ') : String {
    if (str.length >= targetWidth) {
        return str
    }

    val spacesCount = (targetWidth - str.length) / 2;
    var newStr = ""
    return 0.until(targetWidth).map { i ->
        if (i < spacesCount || i >= spacesCount + str.length) {
            padChr
        } else {
            str[i - spacesCount]
        }
    }.joinToString("")
}