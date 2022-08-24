package com.numq.firebasechat.message

import com.numq.firebasechat.interactor.UseCase
import javax.inject.Inject

class ReadMessage @Inject constructor(
    private val repository: MessageRepository
) : UseCase<String, Unit>() {
    override suspend fun execute(arg: String) = repository.readMessage(arg)
}