package com.numq.firebasechat.chat

import arrow.core.Either
import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestChats @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Pair<String, Long>, Flow<Chat>>() {
    override suspend fun execute(arg: Pair<String, Long>): Either<Exception, Flow<Chat>> {
        val (userId, limit) = arg
        return repository.getLatestChats(userId, limit)
    }
}