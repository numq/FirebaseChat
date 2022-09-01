package com.numq.firebasechat.message

import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatService
import com.numq.firebasechat.mapper.chat
import com.numq.firebasechat.mapper.message
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageData @Inject constructor(
    private val chatService: ChatService,
    private val messageService: MessageApi
) : MessageRepository {

    override suspend fun getLatestMessages(chatId: String, limit: Long) =
        messageService.getLatestMessages(chatId, limit)
            .mapNotNull { it.message }
            .wrap()
            .leftIfNull { MessageException }

    override suspend fun getMessages(chatId: String, lastMessageId: String, limit: Long) =
        messageService.getMessages(chatId, lastMessageId, limit)
            .wrap()
            .map { it.documents.mapNotNull { document -> document.message } }
            .leftIfNull { MessageException }

    override suspend fun createMessage(chatId: String, userId: String, text: String) =
        messageService.createMessage(chatId, userId, text)
            .wrap()
            .tap {
                chatService.getChatById(chatId).addOnSuccessListener { document ->
                    document.chat?.let { chat ->
                        chatService.updateChat(chat.copy(lastMessage = it.message))
                    }
                }
            }
            .map { true }

    override suspend fun readMessage(id: String) =
        messageService.readMessage(id)
            .wrap()
            .map { it.message }
            .tap { message ->
                message?.let {
                    chatService.getChatById(message.chatId).addOnSuccessListener { document ->
                        document.chat?.let { chat ->
                            if (chat.lastMessage?.id == message.id) {
                                chatService.updateChat(chat.copy(lastMessage = message))
                            }
                        }
                    }
                }
            }
            .leftIfNull { MessageException }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id)
            .wrap()
            .tap {
                it?.addOnSuccessListener { document ->
                    document.message?.let { message ->
                        chatService.getChatById(message.chatId).addOnSuccessListener { document ->
                            document.chat?.let { chat ->
                                chatService.updateChat(chat.copy(lastMessage = message))
                            }
                        }
                    }
                }
            }.map { id }
}