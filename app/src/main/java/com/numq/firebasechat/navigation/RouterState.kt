package com.numq.firebasechat.navigation

data class RouterState(
    val authenticating: Boolean = true,
    val userId: String? = null,
    val exception: Exception? = null
)