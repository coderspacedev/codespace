package coder.apps.space.library.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import coder.apps.space.library.extension.connectivityManager

object NetworkMonitor {
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null

    fun startMonitoring(context: Context, onNetworkAvailable: (Boolean) -> Unit) {
        connectivityManager = context.connectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = connectivityManager?.getNetworkCapabilities(network)
                val hasInternet =
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && capabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                if (hasInternet) {
                    onNetworkAvailable.invoke(true)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                val activeNetwork = connectivityManager?.activeNetwork
                val capabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
                val hasInternet =
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                if (!hasInternet) {
                    onNetworkAvailable.invoke(false)
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        networkCallback?.let { connectivityManager?.registerNetworkCallback(networkRequest, it) }
    }

    fun stopMonitoring() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }
}