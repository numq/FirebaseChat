package com.numq.firebasechat.user

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class ChangePassword @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, String>, User>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, User> {
        val (id, password) = arg
        return repository.changePassword(id, password)
    }
}