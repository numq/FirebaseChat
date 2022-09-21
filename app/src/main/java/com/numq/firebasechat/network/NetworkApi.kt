package com.numq.firebasechat.network

import kotlinx.coroutines.flow.Flow

interface NetworkApi {
    val state: Flow<NetworkStatus>
    val isAvailable: Boolean
}