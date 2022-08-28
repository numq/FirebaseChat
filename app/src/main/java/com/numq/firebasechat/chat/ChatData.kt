package com.numq.firebasechat.chat

import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.chat
import com.numq.firebasechat.mapper.message
import com.numq.firebasechat.message.MessageApi
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatData @Inject constructor(
    private val chatService: ChatApi,
    private val messageService: MessageApi
) : ChatRepository {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    override suspend fun getLatestChats(userId: String, limit: Long) =
        chatService.getLatestChats(userId, limit)
            .mapNotNull { it.chat }
            .map { chat ->
                var updatedChat: Chat? = null
                coroutineScope.launch {
                    messageService.getLatestMessages(chat.id, 1).collect { msg ->
                        updatedChat = chat.copy(lastMessage = msg.message)
                    }
                }
                updatedChat ?: chat
            }
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