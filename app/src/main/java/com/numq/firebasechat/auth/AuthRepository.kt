package com.numq.firebasechat.auth

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authenticationId: Either<Exception, Flow<String>>
    suspend fun signInByEmail(email: String, password: String): Either<Exception, Flow<AuthResult>>
    suspend fun signUpByEmail(
        name: String,
        email: String,
        password: String
    ): Either<Exception, Flow<AuthResult>>

    suspend fun signOut(): Either<Exception, Flow<AuthResult>>
}