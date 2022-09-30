package com.numq.firebasechat.navigation

import com.numq.firebasechat.auth.AuthenticationState
import com.numq.firebasechat.network.NetworkStatus

data class NavState(
    val status: NetworkStatus? = null,
    val authenticationState: AuthenticationState? = null,
    val exception: Exception? = null
)