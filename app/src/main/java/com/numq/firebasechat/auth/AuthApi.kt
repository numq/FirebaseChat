package com.numq.firebasechat.auth

import kotlinx.coroutines.flow.Flow

interface AuthApi {
    fun getAuthenticationState(): Flow<AuthenticationState>
    fun signInByEmail(email: String, password: String): Unit?
    fun signUpByEmail(
        email: String,
        password: String,
        onSignUp: (String) -> Boolean
    ): Unit?

    fun signOut()
}