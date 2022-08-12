package com.numq.firebasechat.message

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessages @Inject constructor(
    private val repository: MessageRepository
) : UseCase<Triple<String, Long, Long>, Flow<Message>>() {
    override suspend fun execute(arg: Triple<String, Long, Long>): Either<Exception, Flow<Message>> {
        val (chatId, skip, limit) = arg
        return repository.getMessages(chatId, skip, limit)
    }
}