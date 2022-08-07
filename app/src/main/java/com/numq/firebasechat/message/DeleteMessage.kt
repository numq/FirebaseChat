package com.numq.firebasechat.message

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class DeleteMessage @Inject constructor(
    private val repository: MessageRepository
) : UseCase<String, String>() {
    override suspend fun execute(arg: String) = repository.deleteMessage(arg)
}