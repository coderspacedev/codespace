package coder.apps.space.library.extension

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

fun Context.hasPermissions(permissions: Array<String>): Boolean = permissions.all {
    androidx.core.content.ContextCompat.checkSelfPermission(
        this,
        it
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermission(permission: String): Boolean {
    return androidx.core.content.ContextCompat.checkSelfPermission(
        this,
        permission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

fun Context.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun AppCompatActivity.requestPermission(
    permission: String,
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null
): ActivityResultLauncher<String> {
    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) onGranted() else onDenied?.invoke()
        }
    launcher.launch(permission)
    return launcher
}

fun AppCompatActivity.requestPermissions(
    permissions: Array<String>,
    onAllGranted: () -> Unit,
    onDenied: (List<String>) -> Unit
): ActivityResultLauncher<Array<String>> {
    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedPermissions = result.filter { !it.value }.keys.toList()
            if (deniedPermissions.isEmpty()) onAllGranted() else onDenied(deniedPermissions)
        }
    launcher.launch(permissions)
    return launcher
}

fun AppCompatActivity.requestOverlayPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
): ActivityResultLauncher<Intent> {
    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (Settings.canDrawOverlays(this)) onGranted() else onDenied()
        }
    if (!Settings.canDrawOverlays(this)) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = android.net.Uri.parse("package:$packageName")
        }
        launcher.launch(intent)
    } else {
        onGranted()
    }
    return launcher
}