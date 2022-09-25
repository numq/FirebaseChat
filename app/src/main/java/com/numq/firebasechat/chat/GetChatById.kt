package com.numq.firebasechat.chat

import com.numq.firebasechat.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatById @Inject constructor(
    private val repository: ChatRepository
) : UseCase<String, Flow<Chat>>() {
    override suspend fun execute(arg: String) = repository.getChatById(arg)
}