package com.numq.firebasechat.chat

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class CreateChat @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Pair<String, String>, Chat>() {
    override suspend fun execute(arg: Pair<String, String>): Either<Exception, Chat> {
        val (userId, anotherId) = arg
        return repository.createChat(userId, anotherId)
    }
}