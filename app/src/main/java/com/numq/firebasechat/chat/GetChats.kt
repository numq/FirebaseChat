package com.numq.firebasechat.chat

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChats @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Triple<String, String?, Long>, Flow<Chat>>() {
    override suspend fun execute(arg: Triple<String, String?, Long>): Either<Exception, Flow<Chat>> {
        val (userId, lastChatId, limit) = arg
        return repository.getChats(userId, lastChatId, limit)
    }
}