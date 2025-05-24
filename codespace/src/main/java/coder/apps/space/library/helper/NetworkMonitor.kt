package coder.apps.space.library.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import coder.apps.space.library.base.CodeApp
import coder.apps.space.library.extension.isNetworkAvailable
import java.util.concurrent.Executors

object NetworkMonitor {
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null
    private val executor = Executors.newSingleThreadExecutor()

    fun startMonitoring(context: Context, statusCallback: (Boolean) -> Unit) {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                executor.execute {
                    val hasInternet = context.isNetworkAvailable()
                    if (hasInternet) {
                        CodeApp.currentActivity?.runOnUiThread {
                            statusCallback(true)
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                executor.execute {
                    val hasInternet = context.isNetworkAvailable()
                    if (!hasInternet) {
                        CodeApp.currentActivity?.runOnUiThread {
                            statusCallback(false)
                        }
                    }
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun stopMonitoring() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }
}