package coder.apps.space.library.helper

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateHelper(private val activity: Activity?) {

    private val appUpdateManager: AppUpdateManager? = activity?.let {
        AppUpdateManagerFactory.create(it)
    }

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showSnackbarForCompleteUpdate()
        }
    }

    companion object {
        private const val REQUEST_CODE_APP_UPDATE = 9001
    }

    fun checkForUpdate() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                requestUpdate(appUpdateInfo)
            }
        }
        appUpdateManager?.registerListener(listener)
    }

    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        try {
            activity?.let {
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    it,
                    REQUEST_CODE_APP_UPDATE
                )
            }
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_APP_UPDATE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    // Update accepted
                }

                AppCompatActivity.RESULT_CANCELED -> {
                    // Update canceled by user
                }

                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    // Update failed
                }
            }
        }
    }

    private fun showSnackbarForCompleteUpdate() {
        if (activity is AppCompatActivity) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Restart") {
                appUpdateManager?.completeUpdate()
            }.show()
        }
    }

    fun unregisterListener() {
        appUpdateManager?.unregisterListener(listener)
    }
}
