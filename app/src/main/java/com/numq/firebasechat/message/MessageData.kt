package com.numq.firebasechat.message

import arrow.core.flatMap
import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.wrapper.wrap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageData @Inject constructor(
    private val chatService: ChatApi,
    private val messageService: MessageApi
) : MessageRepository {

    override suspend fun getLatestMessages(chatId: String, limit: Long) =
        messageService.getLatestMessages(chatId, limit)
            .wrap()
            .leftIfNull { MessageException }

    override suspend fun getMessages(chatId: String, lastMessageId: String, limit: Long) =
        messageService.getMessages(chatId, lastMessageId, limit)
            .wrap()
            .leftIfNull { MessageException }

    override suspend fun createMessage(chatId: String, userId: String, text: String) =
        messageService.createMessage(chatId, userId, text).wrap()
            .leftIfNull { MessageException }
            .flatMap { message ->
                chatService.getChatById(message.chatId).wrap()
                    .leftIfNull { MessageException }
                    .flatMap { chat ->
                        chatService.updateChat(chat.copy(lastMessage = message)).wrap()
                            .leftIfNull { MessageException }
                            .map { message }
                    }
            }

    override suspend fun readMessage(id: String) =
        messageService.readMessage(id).wrap().leftIfNull { MessageException }.flatMap { message ->
            chatService.getChatById(message.chatId).wrap().map { chat ->
                chat?.let {
                    if (chat.lastMessage?.id == message.id) {
                        chatService.updateChat(chat.copy(lastMessage = message)).wrap()
                    }
                }
                message
            }
        }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id).wrap()
            .leftIfNull { MessageException }
            .flatMap { message ->
                chatService.getChatById(message.chatId).wrap()
                    .leftIfNull { MessageException }
                    .flatMap { chat ->
                        messageService.getMessages(chat.id, id, 1).wrap()
                            .leftIfNull { MessageException }
                            .flatMap {
                                chatService.updateChat(chat.copy(lastMessage = it.lastOrNull()))
                                    .wrap()
                                    .map {
                                        message.id
                                    }
                            }
                    }
            }

}