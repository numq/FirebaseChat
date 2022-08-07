package com.numq.firebasechat.auth

data class AuthState(
    val userId: String? = null,
    val isAuthenticating: Boolean = false,
    val authType: AuthType = AuthType.EmailPassword,
    val exception: Exception? = null
)