package com.numq.firebasechat.message

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastChatMessage @Inject constructor(
    private val repository: MessageRepository
) : UseCase<String, Flow<Message>>() {
    override suspend fun execute(arg: String) = repository.getLastChatMessage(arg)
}