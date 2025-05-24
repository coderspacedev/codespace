package coder.apps.space.library.extension

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import coder.apps.space.library.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.graphics.drawable.toDrawable

// Generic extension for showing a BottomSheetDialog with any ViewBinding
inline fun <T : ViewBinding> Activity.showBottomSheet(
    bindingFactory: (LayoutInflater) -> T, // Factory to inflate the binding
    styleRes: Int = R.style.Theme_Space_BottomSheetDialogTheme, // Default style
    dimAmount: Float = 0.50f, // Default dim amount
    crossinline configure: (T, BottomSheetDialog, () -> Unit) -> Unit, // Configure the binding and dialog
    noinline onDismiss: (() -> Unit)? = null // Optional dismiss callback
) {
    if (isFinishing || isDestroyed) return

    val dialog = BottomSheetDialog(this, styleRes)
    val binding = bindingFactory(layoutInflater)
    dialog.setContentView(binding.root)

    dialog.window?.apply {
        setDimAmount(dimAmount)
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    dialog.setOnShowListener { dialogInterface ->
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val behavior = BottomSheetBehavior.from(sheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
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