package coder.apps.space.library.base

import android.content.Context
import android.content.ContextWrapper
import android.os.*
import android.util.*
import android.view.*
import android.view.WindowInsetsController.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.core.view.*
import androidx.viewbinding.*
import coder.apps.space.library.helper.*
import coder.apps.space.library.R
import coder.apps.space.library.extension.*
import java.util.Locale

abstract class BaseActivity<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
    val isFullScreen: Boolean = false,
    val isFullScreenIncludeNav: Boolean = false,
    private val isLight: Boolean = false,
    private val isLightModified: Boolean = false,
    private val isPadding: Boolean = false,
    private val isNavPadding: Boolean = false,
    private val isRequiredInternet: Boolean? = false
) : AppCompatActivity() {

    var tinyDB: TinyDB? = null
    var screenWidth: Int = 0
    var screenHeight: Int = 0
    var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        tinyDB = TinyDB(this@BaseActivity)
        if (isFullScreen) window?.applyFullScreen()
        initWindows()
        binding = bindingFactory(layoutInflater)
        setContentView(binding?.root)
        initPadding()
        binding?.initExtra()
        binding?.initListeners()
        binding?.initView()
        if (isRequiredInternet == true) setupNetwork()
        initSize()
    }

    private fun setupNetwork() {
        (application as? CodeApp)?.addOnNetworkUpdateListener { hasInternet ->
            if (hasInternet) {
                binding?.initExtra()
                binding?.initListeners()
                binding?.initView()
            } else {
                binding?.initExtra()
                binding?.initListeners()
                binding?.initView()
            }
        }
    }

    private fun initSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    override fun attachBaseContext(newBase: Context) {
        val language = newBase.currentLanguage ?: "en"
        val localeToSwitchTo = Locale(language)
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    abstract fun B.initExtra()
    abstract fun B.initListeners()
    abstract fun B.initView()

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            initWindows()
        }
    }

    private fun initWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, window.decorView).apply {
                isAppearanceLightStatusBars =
                    if (isLightModified) isLight else resources.getBoolean(R.bool.isStatusBarLight)
                isAppearanceLightNavigationBars = isLight
            }
        } else {
            val flags =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            if (isLightModified) {
                if (isLight) window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility or flags
                else window.decorView.systemUiVisibility =
                    (window.decorView.systemUiVisibility.inv() or flags).inv()
            } else {
                if (true) window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility or flags
                else window.decorView.systemUiVisibility =
                    (window.decorView.systemUiVisibility.inv() or flags).inv()
            }
        }
    }

    private fun initPadding() {
        binding?.apply {
            root.setOnApplyWindowInsetsListener { v: View, insets: WindowInsets ->
                v.setPadding(
                    0,
                    isPadding.takeIf { it }?.let { statusBarHeight } ?: 0,
                    0,
                    isNavPadding.takeIf { it }?.let { navigationBarHeight } ?: 0)
                insets
            }
        }
    }

    open fun updateStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@BaseActivity, color)
    }

    open fun updateNavigationBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = ContextCompat.getColor(this@BaseActivity, color)
    }

    private fun Window.applyFullScreen() {
        if (isFullScreenIncludeNav) {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } else {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            )
        }
        removeControlBar()
    }

    fun Window.showControlBar() {
        val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    fun Window.removeControlBar() {
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    fun incrementPermissionsDeniedCount(type: String) {
        val currentCount = tinyDB?.getInt(type, 0) ?: 0
        val newCount = currentCount + 1
        TinyDB(this@BaseActivity).putInt(type, newCount)
    }

    fun fetchPermissionsDeniedCount(type: String): Int {
        return tinyDB?.getInt(type, 0) ?: 0
    }
}