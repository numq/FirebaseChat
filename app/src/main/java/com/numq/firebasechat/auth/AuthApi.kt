package com.numq.firebasechat.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    val authenticationState: Boolean
    val authenticationId: Flow<String?>
    fun signInByEmail(email: String, password: String): Task<AuthResult>
    fun signUpByEmail(email: String, password: String): Task<AuthResult>
    fun signOut()
}