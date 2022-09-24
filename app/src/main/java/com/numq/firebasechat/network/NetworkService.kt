package com.numq.firebasechat.network

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkService @Inject constructor(
    context: Application
) : NetworkApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    private val connectivityManager: ConnectivityManager? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else {
            context.getSystemService(Application.CONNECTIVITY_SERVICE) as? ConnectivityManager
        }

    private val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    override val state = callbackFlow {
        val sendStatus: ProducerScope<NetworkStatus>.(NetworkStatus) -> Unit = {
            coroutineScope.launch {
                send(it)
            }
        }
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                sendStatus(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                sendStatus(NetworkStatus.Unavailable)
            }
        }
        connectivityManager?.registerNetworkCallback(request, networkCallback)
        sendStatus(if (isAvailable) NetworkStatus.Available else NetworkStatus.Unavailable)
        awaitClose {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

    override val isAvailable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    else -> false
                }
            }
        } ?: false
    } else {
        connectivityManager?.activeNetworkInfo?.run {
            when (type) {
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_WIFI -> true
                else -> false
            }
        } ?: false
    }
}