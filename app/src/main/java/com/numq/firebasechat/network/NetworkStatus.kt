package com.numq.firebasechat.network

sealed interface NetworkStatus {
    object Available : NetworkStatus
    object Unavailable : NetworkStatus
}