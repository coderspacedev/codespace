package coder.apps.space.library.base

import coder.apps.space.library.R
import android.app.*
import android.content.*
import android.os.*
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.*
import androidx.viewbinding.*
import coder.apps.space.library.extension.*
import com.google.android.material.bottomsheet.*

abstract class BaseBottomSheetDialog<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
    val isLight: Boolean = false,
    private val isLightModified: Boolean = false,
    val isPadding: Boolean = false,
    private val isRequiredInternet: Boolean? = false
) : BottomSheetDialogFragment() {

    var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Space_BottomSheetDialogFragmentTheme)
        (dialog as? BottomSheetDialog)?.dismissWithAnimation = true
        create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingFactory(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isPadding) initPadding()
        binding?.let {
            it.viewCreated()
            it.initListeners()
            it.initView()
        }
        if (isRequiredInternet == true) setupNetwork()
    }

    private fun setupNetwork() {
        activity?.apply {
            (application as? CodeApp)?.addOnNetworkUpdateListener { hasInternet ->
                if (hasInternet) {
                    binding?.let {
                        it.viewCreated()
                        it.initListeners()
                        it.initView()
                    }
                } else {
                    binding?.let {
                        it.viewCreated()
                        it.initListeners()
                        it.initView()
                    }
                }
            }
        }
    }

    private fun initWindows() {
        dialog?.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsControllerCompat(this, decorView).apply {
                    isAppearanceLightStatusBars =
                        if (isLightModified) isLight else resources.getBoolean(R.bool.isStatusBarLight)
                    isAppearanceLightNavigationBars = isLight
                }
            } else {
                val flags =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                if (isLightModified) {
                    if (isLight) decorView.systemUiVisibility =
                        decorView.systemUiVisibility or flags
                    else decorView.systemUiVisibility =
                        (decorView.systemUiVisibility.inv() or flags).inv()
                } else {
                    decorView.systemUiVisibility = decorView.systemUiVisibility or flags
                }
            }
        }
    }

    private fun initPadding() {
        activity?.apply {
            binding?.apply {
                root.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
                    v.setPadding(0, statusBarHeight, 0, 0)
                    insets
                }
            }
        }
    }

    abstract fun B.viewCreated()
    abstract fun B.initListeners()
    abstract fun B.initView()
    abstract fun create()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity = requireActivity()
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

    override fun onStart() {
        super.onStart()
        initWindows()
        dialog?.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                attributes?.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }
}