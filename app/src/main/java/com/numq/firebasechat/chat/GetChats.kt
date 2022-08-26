package com.numq.firebasechat.chat

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class GetChats @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Triple<String, String, Long>, List<Chat>>() {
    override suspend fun execute(arg: Triple<String, String, Long>): Either<Exception, List<Chat>> {
        val (userId, lastChatId, limit) = arg
        return repository.getChats(userId, lastChatId, limit)
    }
}