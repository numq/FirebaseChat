package com.numq.firebasechat.chat

import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.chat
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatData @Inject constructor(
    private val chatService: ChatApi
) : ChatRepository {

    override suspend fun getLatestChats(userId: String, limit: Long) =
        chatService.getLatestChats(userId, limit)
            .mapNotNull { it.chat }
            .wrap()
            .leftIfNull { ChatException }

    override suspend fun getChats(userId: String, lastChatId: String, limit: Long) =
        chatService.getChats(userId, lastChatId, limit)
            .wrap()
            .map { it.documents.mapNotNull { document -> document.chat } }
            .leftIfNull { ChatException }

    override suspend fun getChatById(id: String) =
        chatService.getChatById(id)
            .wrap()
            .map { it.chat }
            .leftIfNull { ChatException }

    override suspend fun createChat(userId: String, anotherId: String) =
        chatService.createChat(userId, anotherId)
            .wrap()
            .map { it.chat }
            .leftIfNull { ChatException }

    override suspend fun updateChat(chat: Chat) =
        chatService.updateChat(chat)
            .wrap()
            .map { it.chat }
            .leftIfNull { ChatException }

    override suspend fun deleteChat(id: String) =
        chatService.deleteChat(id)
            .wrap()
            .map { id }

}