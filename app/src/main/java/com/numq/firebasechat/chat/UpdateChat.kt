package com.numq.firebasechat.chat

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class UpdateChat @Inject constructor(
    private val repository: ChatRepository
) : UseCase<Chat, Chat>() {
    override suspend fun execute(arg: Chat) = repository.updateChat(arg)
}