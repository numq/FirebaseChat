package com.numq.firebasechat.chat

import arrow.core.leftIfNull
import com.numq.firebasechat.wrapper.wrap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatData @Inject constructor(
    private val chatService: ChatApi
) : ChatRepository {

    override suspend fun getChats(userId: String, lastChatId: String?, limit: Long) =
        chatService.getChats(userId, lastChatId, limit)
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun getChatById(id: String) =
        chatService.getChatById(id)
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun createChat(userId: String, anotherId: String) =
        chatService.createChat(userId, anotherId)
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun updateChat(chat: Chat) =
        chatService.updateChat(chat)
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun deleteChat(id: String) =
        chatService.deleteChat(id)
            .wrap()
            .map { id }

}