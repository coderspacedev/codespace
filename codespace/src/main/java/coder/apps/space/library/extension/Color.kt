package coder.apps.space.library.extension

import android.graphics.Color

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import kotlin.math.pow

/**
 * Data class for RGB color components.
 */
data class RGB(val red: Int, val green: Int, val blue: Int)

/**
 * Converts hex string (e.g., "#FF0000") to Color Int with default fallback.
 * @param defaultColor Fallback color if parsing fails.
 * @return Color integer.
 */
@ColorInt
fun String.parseColorWithDefault(@ColorInt defaultColor: Int = Color.BLACK): Int {
    return try {
        Color.parseColor(this)
    } catch (e: IllegalArgumentException) {
        defaultColor
    }
}

/**
 * Converts hex string (e.g., "#FF0000") to Color Int (backward compatible).
 */
@ColorInt
fun String.toColorInt(): Int = parseColorWithDefault()

/**
 * Converts Color Int to hex string (e.g., "#FF0000").
 * @param includeAlpha Include alpha channel if true.
 * @return Hex string.
 */
fun Int.toHexColor(includeAlpha: Boolean = false): String {
    return if (includeAlpha) {
        String.format("#%08X", this)
    } else {
        String.format("#%06X", (0xFFFFFF and this))
    }
}

/**
 * Adjusts alpha of a Color Int (0.0f to 1.0f).
 * @param alpha Alpha value (0.0f = transparent, 1.0f = opaque).
 * @return Color with adjusted alpha.
 */
@ColorInt
fun Int.withAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
    val clampedAlpha = alpha.coerceIn(0f, 1f)
    return Color.argb(
        (clampedAlpha * 255).toInt(),
        Color.red(this),
        Color.green(this),
        Color.blue(this)
    )
}

/**
 * Lightens a color by a percentage (0.0f to 1.0f).
 * @param factor Lightening factor.
 * @return Lightened color.
 */
@ColorInt
fun Int.lighten(@FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
    val r = (Color.red(this) + (255 - Color.red(this)) * factor.coerceIn(0f, 1f)).toInt()
    val g = (Color.green(this) + (255 - Color.green(this)) * factor.coerceIn(0f, 1f)).toInt()
    val b = (Color.blue(this) + (255 - Color.blue(this)) * factor.coerceIn(0f, 1f)).toInt()
    return Color.argb(Color.alpha(this), r, g, b)
}

/**
 * Darkens a color by a percentage (0.0f to 1.0f).
 * @param factor Darkening factor.
 * @return Darkened color.
 */
@ColorInt
fun Int.darken(@FloatRange(from = 0.0, to = 1.0) factor: Float): Int {
    val r = (Color.red(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    val g = (Color.green(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    val b = (Color.blue(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    return Color.argb(Color.alpha(this), r, g, b)
}

/**
 * Checks if a color is dark based on luminance.
 * @return True if the color is dark.
 */
fun Int.isDark(): Boolean {
    val luminance = 0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)
    return luminance < 128
}

/**
 * Gets the complementary color.
 * @return Complementary color.
 */
@ColorInt
fun Int.getComplementary(): Int {
    return Color.argb(
        Color.alpha(this),
        255 - Color.red(this),
        255 - Color.green(this),
        255 - Color.blue(this)
    )
}

/**
 * Blends two colors with a ratio (0.0f = full this, 1.0f = full color2).
 * @param color2 Second color.
 * @param ratio Blend ratio.
 * @return Blended color.
 */
@ColorInt
fun Int.blendWith(@ColorInt color2: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
    val r = (Color.red(this) * (1f - ratio) + Color.red(color2) * ratio).toInt()
    val g = (Color.green(this) * (1f - ratio) + Color.green(color2) * ratio).toInt()
    val b = (Color.blue(this) * (1f - ratio) + Color.blue(color2) * ratio).toInt()
    val a = (Color.alpha(this) * (1f - ratio) + Color.alpha(color2) * ratio).toInt()
    return Color.argb(a, r, g, b)
}

/**
 * Converts Color Int to RGB components.
 * @return RGB data class.
 */
fun Int.toRGB(): RGB = RGB(Color.red(this), Color.green(this), Color.blue(this))

/**
 * Adjusts color saturation (0.0f = grayscale, 1.0f = original, >1.0f = more vivid).
 * @param factor Saturation factor.
 * @return Color with adjusted saturation.
 */
@ColorInt
fun Int.adjustSaturation(@FloatRange(from = 0.0) factor: Float): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    hsv[1] = (hsv[1] * factor.coerceAtLeast(0f)).coerceAtMost(1f)
    return Color.HSVToColor(Color.alpha(this), hsv)
}

/**
 * Calculates contrast ratio between two colors (WCAG standard).
 * @param otherColor Second color.
 * @return Contrast ratio (1.0 to 21.0).
 */
fun Int.contrastRatio(@ColorInt otherColor: Int): Float {
    val lum1 = luminance()
    val lum2 = otherColor.luminance()
    val brightest = maxOf(lum1, lum2)
    val darkest = minOf(lum1, lum2)
    return (brightest + 0.05f) / (darkest + 0.05f)
}

/**
 * Generates a monochromatic color palette.
 * @param count Number of colors to generate.
 * @return List of colors.
 */
fun Int.generateMonochromaticPalette(count: Int): List<Int> {
    return (1..count.coerceAtLeast(1)).map { i ->
        val factor = i.toFloat() / (count + 1)
        if (isDark()) lighten(factor) else darken(factor)
    }
}

/**
 * Calculates luminance of a color (WCAG formula).
 * @return Luminance value (0.0 to 1.0).
 */
private fun Int.luminance(): Float {
    val r = Color.red(this) / 255f
    val g = Color.green(this) / 255f
    val b = Color.blue(this) / 255f
    return (0.2126f * r.pow(2.2f) + 0.7152f * g.pow(2.2f) + 0.0722f * b.pow(2.2f))
}