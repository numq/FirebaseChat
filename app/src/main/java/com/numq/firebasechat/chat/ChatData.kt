package com.numq.firebasechat.chat

import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import javax.inject.Inject

class ChatData @Inject constructor(
    private val networkService: NetworkApi,
    private val chatService: ChatApi
) : ChatRepository {

    override suspend fun getChats(userId: String, lastChatId: String?, limit: Long) =
        chatService.getChats(userId, lastChatId, limit)
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun getChatById(id: String) =
        chatService.getChatById(id)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { ChatException }

    override suspend fun createChat(userId: String, anotherId: String) =
        chatService.createChat(userId, anotherId)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { ChatException }

    override suspend fun updateChat(chat: Chat) =
        chatService.updateChat(chat)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { ChatException }

    override suspend fun deleteChat(id: String) =
        chatService.deleteChat(id)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .map { id }

}