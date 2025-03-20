package coder.apps.space.library.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getDrawableRes(drawableRes: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableRes)
}

fun Drawable.tint(color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this.mutate())
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}

fun Drawable.tint(context: Context, colorRes: Int): Drawable {
    return tint(ContextCompat.getColor(context, colorRes))
}

fun Drawable.toBitmap(width: Int = intrinsicWidth, height: Int = intrinsicHeight): Bitmap {
    if (this is BitmapDrawable && bitmap != null) return bitmap

    val bitmap = Bitmap.createBitmap(
        if (width <= 0) 1 else width,
        if (height <= 0) 1 else height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

fun Drawable.resize(context: Context, width: Int, height: Int): Drawable {
    val bitmap = toBitmap(width, height)
    return BitmapDrawable(context.resources, bitmap)
}

fun Drawable.withAlpha(alpha: Int): Drawable {
    val drawable = mutate()
    drawable.alpha = alpha.coerceIn(0, 255)
    return drawable
}

fun Drawable.applyColorFilter(
    color: Int,
    mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
): Drawable {
    val drawable = mutate()
    drawable.setColorFilter(color, mode)
    return drawable
}

fun Drawable?.isNullOrEmpty(): Boolean {
    return this == null || (intrinsicWidth <= 1 && intrinsicHeight <= 1)
}

fun Drawable.rotate(context: Context, degrees: Float): Drawable {
    val bitmap = toBitmap()
    val matrix = android.graphics.Matrix().apply { postRotate(degrees) }
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    return BitmapDrawable(context.resources, rotatedBitmap)
}