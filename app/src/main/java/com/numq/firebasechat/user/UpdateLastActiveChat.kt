package com.numq.firebasechat.user

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class UpdateLastActiveChat @Inject constructor(
    private val repository: UserRepository
) : UseCase<Pair<String, String>, Unit>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, Unit> {
        val (userId, chatId) = arg
        return repository.updateLastActiveChat(userId, chatId)
    }
}