package com.numq.firebasechat.auth

import arrow.core.leftIfNull
import com.numq.firebasechat.user.UserApi
import com.numq.firebasechat.wrapper.wrap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthData @Inject constructor(
    private val authService: AuthApi,
    private val userService: UserApi
) : AuthRepository {

    override val authenticationId = authService.authenticationId.wrap()

    override suspend fun signInByEmail(email: String, password: String) =
        authService.signInByEmail(email, password).wrap().leftIfNull { AuthException }

    override suspend fun signUpByEmail(name: String, email: String, password: String) =
        authService.signUpByEmail(email, password) { user ->
            user?.uid?.let { id ->
                userService.createUser(id, email).addOnSuccessListener {
                    userService.updateName(id, name)
                }
            }
        }.wrap().leftIfNull { AuthException }

    override suspend fun signOut() = authService.signOut().wrap()

}