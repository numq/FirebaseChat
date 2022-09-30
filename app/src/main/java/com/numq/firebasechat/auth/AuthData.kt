package com.numq.firebasechat.auth

import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.user.UserApi
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import javax.inject.Inject

class AuthData @Inject constructor(
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