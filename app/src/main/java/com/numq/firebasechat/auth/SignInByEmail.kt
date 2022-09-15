package com.numq.firebasechat.auth

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class SignInByEmail @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Pair<String, String>, Boolean>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, Boolean> {
        val (email, password) = arg
        return repository.signInByEmail(email, password)
    }
}