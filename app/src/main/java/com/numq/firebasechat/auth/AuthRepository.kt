package com.numq.firebasechat.auth

import arrow.core.Either
import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.user.UserApi
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    class Implementation @Inject constructor(
        private val networkService: NetworkApi,
        private val authService: AuthApi,
        private val userService: UserApi
    ) : AuthRepository {

        override suspend fun getAuthenticationState() =
            authService.getAuthenticationState()
                .wrap()
                .leftIfNull { AuthException }

        override suspend fun signInByEmail(email: String, password: String) =
            authService.signInByEmail(email, password)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { AuthException }

        override suspend fun signUpByEmail(name: String, email: String, password: String) =
            authService.signUpByEmail(name, email, password) { id ->
                runCatching { userService.createUser(id, name, email).isSuccessful }.isSuccess
            }.wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { AuthException }

        override suspend fun signOut() =
            authService.signOut()
                .wrapIf(networkService.isAvailable, NetworkException.Default)

    }

}