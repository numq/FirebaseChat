package com.numq.firebasechat.message

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class SendMessage @Inject constructor(
    private val repository: MessageRepository
) : UseCase<Triple<String, String, String>, Message>() {
    override suspend fun execute(arg: Triple<String, String, String>): Either<Exception, Message> {
        val (chatId, userId, text) = arg
        return repository.createMessage(chatId, userId, text)
    }
}