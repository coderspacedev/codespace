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

// Generic extension for showing a BottomSheetDialog with any ViewBinding
inline fun <T : ViewBinding> Activity.showBottomSheet(
    bindingFactory: (LayoutInflater) -> T, // Factory to inflate the binding
    styleRes: Int = R.style.Theme_Space_BottomSheetDialogTheme, // Default style
    dimAmount: Float = 0.50f, // Default dim amount
    crossinline configure: (T, BottomSheetDialog) -> Unit, // Configure the binding and dialog
    noinline onDismiss: (() -> Unit)? = null // Optional dismiss callback
) {
    if (isFinishing || isDestroyed) return

    val dialog = BottomSheetDialog(this, styleRes)
    val binding = bindingFactory(layoutInflater)
    dialog.setContentView(binding.root)

    // Common dialog window setup
    dialog.window?.apply {
        setDimAmount(dimAmount)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    // Ensure the bottom sheet is fully expanded
    dialog.setOnShowListener { dialogInterface ->
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val behavior = BottomSheetBehavior.from(sheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    // Track if an action (like "Continue") was taken to avoid triggering dismiss
    var isActionTaken = false

    dialog.setOnDismissListener {
        if (!isActionTaken) {
            onDismiss?.invoke()
        }
    }

    // Pass binding and dialog to the configuration lambda
    configure(binding, dialog)

    dialog.show()
}

// Optional: Simplified version for cases with Continue/Dismiss buttons
inline fun <T : ViewBinding> Activity.showActionBottomSheet(
    bindingFactory: (LayoutInflater) -> T,
    styleRes: Int = R.style.Theme_Space_BottomSheetDialogTheme,
    dimAmount: Float = 0.50f,
    crossinline configureBinding: (T) -> Unit = {}, // Optional binding config
    noinline onContinue: () -> Unit,
    noinline onDismiss: (() -> Unit)? = null
) {
    showBottomSheet(
        bindingFactory = bindingFactory,
        styleRes = styleRes,
        dimAmount = dimAmount,
        configure = { binding, dialog ->
            configureBinding(binding)
            binding.root.findViewById<View>(android.R.id.button1)?.setOnClickListener {
                dialog.dismiss()
                onContinue.invoke()
            }
        },
        onDismiss = onDismiss
    )
}