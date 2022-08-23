package com.numq.firebasechat.auth

sealed class AuthResult {
    object Authenticating : AuthResult()
    object Success : AuthResult()
    object Failure : AuthResult()
}