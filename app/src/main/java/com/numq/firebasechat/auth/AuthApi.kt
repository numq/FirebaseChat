package com.numq.firebasechat.auth

import kotlinx.coroutines.flow.Flow

interface AuthApi {
    fun getAuthenticationState(): Flow<AuthenticationState>
    fun signInByEmail(email: String, password: String): Boolean
    fun signUpByEmail(
        name: String,
        email: String,
        password: String,
        onSignUp: (String) -> Boolean
    ): Boolean

    fun signOut(): Boolean
}