package com.numq.firebasechat.auth

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class SignUpByEmail @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Pair<String, String>, String>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, String> {
        val (email, password) = arg
        return repository.signUpByEmail(email, password)
    }
}