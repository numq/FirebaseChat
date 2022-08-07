package com.numq.firebasechat.chat

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class DeleteChat @Inject constructor(
    private val repository: ChatRepository
) : UseCase<String, String>() {
    override suspend fun execute(arg: String) = repository.deleteChat(arg)
}