package coder.apps.space.library.extension

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

fun Context.getTypefaceFromRes(fontRes: Int): Typeface? {
    return try {
        ResourcesCompat.getFont(this, fontRes)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.getTypefaceFromAssets(assetPath: String): Typeface? {
    return try {
        Typeface.createFromAsset(assets, assetPath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun TextView.setCustomTypeface(typeface: Typeface?) {
    typeface?.let { this.typeface = it }
}

fun TextView.setTypefaceFromRes(context: Context, fontRes: Int) {
    val typeface = context.getTypefaceFromRes(fontRes)
    setCustomTypeface(typeface)
}

fun TextView.setTypefaceFromAssets(context: Context, assetPath: String) {
    val typeface = context.getTypefaceFromAssets(assetPath)
    setCustomTypeface(typeface)
}

fun Typeface.withBold(): Typeface {
    return Typeface.create(this, style or Typeface.BOLD)
}

fun Typeface.withItalic(): Typeface {
    return Typeface.create(this, style or Typeface.ITALIC)
}

fun Typeface.withNormal(): Typeface {
    return Typeface.create(this, Typeface.NORMAL)
}

fun Typeface.withBoldItalic(): Typeface {
    return Typeface.create(this, Typeface.BOLD_ITALIC)
}

fun Typeface.isBold(): Boolean {
    return style and Typeface.BOLD != 0
}

fun Typeface.isItalic(): Boolean {
    return style and Typeface.ITALIC != 0
}