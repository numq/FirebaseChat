package com.numq.firebasechat.auth

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInByEmail @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Pair<String, String>, Flow<AuthResult>>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, Flow<AuthResult>> {
        val (email, password) = arg
        return repository.signInByEmail(email, password)
    }
}