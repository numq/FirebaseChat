package com.numq.firebasechat.message

import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.message
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageData @Inject constructor(
    private val messageService: MessageApi
) : MessageRepository {

    override suspend fun getMessages(chatId: String, limit: Long) =
        messageService.getMessages(chatId, limit)
            .wrap()
            .map { it.asFlow().mapNotNull { document -> document.message } }
            .leftIfNull { MessageException }

    override suspend fun getMessageById(id: String) =
        messageService.getMessageById(id)
            .wrap()
            .map { it.message }
            .leftIfNull { MessageException }

    override suspend fun createMessage(chatId: String, userId: String, text: String) =
        messageService.createMessage(chatId, userId, text)
            .wrap()
            .map { true }

    override suspend fun updateMessage(id: String, text: String) =
        messageService.updateMessage(id, text)
            .wrap()
            .map { true }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id)
            .wrap()
            .map { id }

}