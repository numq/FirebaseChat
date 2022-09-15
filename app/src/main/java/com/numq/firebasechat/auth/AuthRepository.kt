package com.numq.firebasechat.auth

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getAuthenticationState(): Either<Exception, Flow<AuthenticationState>>
    suspend fun signInByEmail(
        email: String,
        password: String
    ): Either<Exception, Boolean>

    suspend fun signUpByEmail(
        name: String,
        email: String,
        password: String
    ): Either<Exception, Boolean>

    suspend fun signOut(): Either<Exception, Boolean>
}