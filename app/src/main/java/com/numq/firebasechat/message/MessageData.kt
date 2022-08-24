package com.numq.firebasechat.message

import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.mapper.message
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageData @Inject constructor(
    private val messageService: MessageApi,
    private val chatService: ChatApi
) : MessageRepository {

    override suspend fun getMessages(chatId: String, skip: Long, limit: Long) =
        messageService.getMessages(chatId, skip, limit)
            .mapNotNull { it.message }
            .wrap()
            .leftIfNull { MessageException }

    override suspend fun getMessageById(id: String) =
        messageService.getMessageById(id)
            .wrap()
            .map { it.message }
            .leftIfNull { MessageException }

    override suspend fun createMessage(chatId: String, userId: String, text: String) =
        messageService.createMessage(chatId, userId, text)
            .wrap()
            .tap { it.message?.let { message -> chatService.updateLastMessage(chatId, message) } }
            .map { true }

    override suspend fun readMessage(id: String, userId: String) =
        messageService.readMessage(id, userId)
            .wrap()
            .map { Unit }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id)
            .wrap()
            .map { id }

}