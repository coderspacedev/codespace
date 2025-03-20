package coder.apps.space.library.extension

import android.graphics.Color
import android.content.Context
import androidx.core.content.ContextCompat

// Get color from resources safely
fun Context.getColorRes(colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

// Convert hex string (e.g., "#FF0000") to Color Int
fun String.toColorInt(): Int {
    return try {
        Color.parseColor(this)
    } catch (e: IllegalArgumentException) {
        Color.BLACK // Default fallback
    }
}

// Convert Color Int to hex string (e.g., "#FF0000")
fun Int.toHexColor(includeAlpha: Boolean = false): String {
    return if (includeAlpha) {
        String.format("#%08X", this)
    } else {
        String.format("#%06X", (0xFFFFFF and this))
    }
}

// Adjust alpha of a Color Int (0.0f to 1.0f)
fun Int.withAlpha(alpha: Float): Int {
    val clampedAlpha = alpha.coerceIn(0f, 1f)
    val alphaInt = (clampedAlpha * 255).toInt()
    return Color.argb(alphaInt, Color.red(this), Color.green(this), Color.blue(this))
}

// Lighten a color by a percentage (0.0f to 1.0f)
fun Int.lighten(factor: Float): Int {
    val r = (Color.red(this) + (255 - Color.red(this)) * factor.coerceIn(0f, 1f)).toInt()
    val g = (Color.green(this) + (255 - Color.green(this)) * factor.coerceIn(0f, 1f)).toInt()
    val b = (Color.blue(this) + (255 - Color.blue(this)) * factor.coerceIn(0f, 1f)).toInt()
    return Color.argb(Color.alpha(this), r, g, b)
}

// Darken a color by a percentage (0.0f to 1.0f)
fun Int.darken(factor: Float): Int {
    val r = (Color.red(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    val g = (Color.green(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    val b = (Color.blue(this) * (1f - factor.coerceIn(0f, 1f))).toInt()
    return Color.argb(Color.alpha(this), r, g, b)
}

// Check if a color is dark (based on luminance)
fun Int.isDark(): Boolean {
    val luminance = (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this))
    return luminance < 128
}

// Get complementary color
fun Int.getComplementary(): Int {
    val r = 255 - Color.red(this)
    val g = 255 - Color.green(this)
    val b = 255 - Color.blue(this)
    return Color.argb(Color.alpha(this), r, g, b)
}

// Blend two colors with a ratio (0.0f = full color1, 1.0f = full color2)
fun Int.blendWith(color2: Int, ratio: Float): Int {
    val r = (Color.red(this) * (1f - ratio) + Color.red(color2) * ratio).toInt()
    val g = (Color.green(this) * (1f - ratio) + Color.green(color2) * ratio).toInt()
    val b = (Color.blue(this) * (1f - ratio) + Color.blue(color2) * ratio).toInt()
    val a = (Color.alpha(this) * (1f - ratio) + Color.alpha(color2) * ratio).toInt()
    return Color.argb(a, r, g, b)
}