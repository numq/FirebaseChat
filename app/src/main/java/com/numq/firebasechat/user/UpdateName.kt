package com.numq.firebasechat.user

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class UpdateName @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, String>, User>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, User> {
        val (id, name) = arg
        return repository.updateName(id, name)
    }
}