package com.numq.firebasechat.auth

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class SignUpByEmail @Inject constructor(
    private val repository: AuthRepository
) : UseCase<Triple<String, String, String>, Boolean>() {
    override suspend fun execute(arg: Triple<String, String, String>): Either<Exception, Boolean> {
        val (name, email, password) = arg
        return repository.signUpByEmail(name, email, password)
    }
}