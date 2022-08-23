package com.numq.firebasechat.navigation

data class RouterState(
    val isLoading: Boolean = true,
    val userId: String? = null,
    val exception: Exception? = null
)