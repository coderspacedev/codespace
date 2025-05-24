package coder.apps.space.library.extension

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import coder.apps.space.library.R
import androidx.core.graphics.drawable.toDrawable

inline fun <T : ViewBinding> Activity.showDialog(
    bindingFactory: (LayoutInflater) -> T,
    styleRes: Int = R.style.Theme_Space_Dialog,
    dimAmount: Float = 0.50f,
    width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    gravity: Int = Gravity.CENTER,
    crossinline configure: (T, Dialog, () -> Unit) -> Unit,
    noinline onDismiss: (() -> Unit)? = null
) {
    if (isFinishing || isDestroyed) return

    val dialog = Dialog(this, styleRes)
    val binding = bindingFactory(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.window?.apply {
        val params = attributes
        params.width = dimen(width).toInt()
        params.height = height
        params.gravity = gravity
        attributes = params
        setDimAmount(dimAmount)
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    var isActionTaken = false
    val setActionTaken = { isActionTaken = true }

    dialog.setOnDismissListener {
        if (!isActionTaken) {
            onDismiss?.invoke()
        }
    }

    configure(binding, dialog, setActionTaken)

    dialog.show()
}