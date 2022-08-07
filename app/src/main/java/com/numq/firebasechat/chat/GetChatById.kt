package com.numq.firebasechat.chat

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class GetChatById @Inject constructor(
    private val repository: ChatRepository
) : UseCase<String, Chat>() {
    override suspend fun execute(arg: String) = repository.getChatById(arg)
}