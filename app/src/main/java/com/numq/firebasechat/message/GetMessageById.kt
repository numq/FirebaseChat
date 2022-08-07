package com.numq.firebasechat.message

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class GetMessageById @Inject constructor(
    private val repository: MessageRepository
) : UseCase<String, Message>() {
    override suspend fun execute(arg: String) = repository.getMessageById(arg)
}