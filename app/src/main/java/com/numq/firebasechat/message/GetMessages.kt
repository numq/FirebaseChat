package com.numq.firebasechat.message

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessages @Inject constructor(
    private val repository: MessageRepository
) : UseCase<Triple<String, String, Long>, List<Message>>() {
    override suspend fun execute(arg: Triple<String, String, Long>): Either<Exception, List<Message>> {
        val (chatId, lastMessageId, limit) = arg
        return repository.getMessages(chatId, lastMessageId, limit)
    }
}