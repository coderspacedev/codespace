package coder.apps.space.library.extension

import android.graphics.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import androidx.core.view.*

fun Window.applyMaterialConfig(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(this, false)
        val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    } else {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        statusBarColor = Color.TRANSPARENT
    }
}

fun Window.applyDialogConfig() {
    val params = attributes
    params?.width = WindowManager.LayoutParams.MATCH_PARENT
    params?.height = WindowManager.LayoutParams.WRAP_CONTENT
    params?.gravity = Gravity.CENTER
    attributes = params
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    setDimAmount(.50f)
    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}