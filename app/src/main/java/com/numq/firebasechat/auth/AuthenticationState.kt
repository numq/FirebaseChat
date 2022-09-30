package com.numq.firebasechat.auth

sealed class AuthenticationState {
    object Authenticating : AuthenticationState()
    data class Authenticated(val userId: String) : AuthenticationState()
    object Unauthenticated : AuthenticationState()
}