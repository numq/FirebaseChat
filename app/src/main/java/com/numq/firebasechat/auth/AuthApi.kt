package com.numq.firebasechat.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    val authenticationId: Flow<String>
    fun signInByEmail(email: String, password: String): Flow<AuthResult>
    fun signUpByEmail(
        email: String,
        password: String,
        onSignUp: (FirebaseUser?) -> Unit
    ): Flow<AuthResult>

    fun signOut(): Flow<AuthResult>
}