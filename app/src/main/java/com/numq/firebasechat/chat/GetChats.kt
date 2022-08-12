package com.numq.firebasechat.chat

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChats @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Triple<String, Long, Long>, Flow<Chat>>() {
    override suspend fun execute(arg: Triple<String, Long, Long>): Either<Exception, Flow<Chat>> {
        val (userId, offset, limit) = arg
        return repository.getChats(userId, offset, limit)
    }
}