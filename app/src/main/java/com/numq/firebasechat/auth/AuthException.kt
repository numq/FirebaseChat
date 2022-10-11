package com.numq.firebasechat.auth

sealed interface AuthException {
    object Default : AuthException, Exception("Failed to connect to auth service")
}