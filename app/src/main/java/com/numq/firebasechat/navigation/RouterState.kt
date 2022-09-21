package com.numq.firebasechat.navigation

import com.numq.firebasechat.network.NetworkStatus

data class RouterState(
    val authenticating: Boolean = true,
    val userId: String? = null,
    val status: NetworkStatus? = null,
    val exception: Exception? = null
)