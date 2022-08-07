package com.numq.firebasechat.message

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessages @Inject constructor(
    private val repository: MessageRepository
) : UseCase<Pair<String, Long>, Flow<Message>>() {
    override suspend fun execute(arg: Pair<String, Long>): Either<Exception, Flow<Message>> {
        val (chatId, limit) = arg
        return repository.getMessages(chatId, limit)
    }
}