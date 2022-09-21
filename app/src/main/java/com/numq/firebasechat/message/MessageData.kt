package com.numq.firebasechat.message

import arrow.core.flatMap
import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.wrapper.wrap
import javax.inject.Inject

class MessageData @Inject constructor(
    private val networkService: NetworkApi,
    private val chatService: ChatApi,
    private val messageService: MessageApi
) : MessageRepository {

    override suspend fun getLatestMessages(chatId: String, limit: Long) =
        messageService.getLatestMessages(chatId, limit)
            .wrap(networkService)
            .leftIfNull { MessageException }

    override suspend fun getMessages(chatId: String, lastMessageId: String, limit: Long) =
        messageService.getMessages(chatId, lastMessageId, limit)
            .wrap(networkService)
            .leftIfNull { MessageException }

    override suspend fun createMessage(chatId: String, userId: String, text: String) =
        messageService.createMessage(chatId, userId, text).wrap(networkService)
            .leftIfNull { MessageException }
            .flatMap { message ->
                chatService.getChatById(message.chatId).wrap(networkService)
                    .leftIfNull { MessageException }
                    .flatMap { chat ->
                        chatService.updateChat(chat.copy(lastMessage = message))
                            .wrap(networkService)
                            .leftIfNull { MessageException }
                            .map { message }
                    }
            }

    override suspend fun readMessage(id: String) =
        messageService.readMessage(id).wrap(networkService).leftIfNull { MessageException }
            .flatMap { message ->
                chatService.getChatById(message.chatId).wrap(networkService).map { chat ->
                    chat?.let {
                        if (chat.lastMessage?.id == message.id) {
                            chatService.updateChat(chat.copy(lastMessage = message))
                                .wrap(networkService)
                        }
                    }
                    message
                }
            }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id).wrap(networkService)
            .leftIfNull { MessageException }
            .flatMap { message ->
                chatService.getChatById(message.chatId).wrap(networkService)
                    .leftIfNull { MessageException }
                    .flatMap { chat ->
                        messageService.getMessages(chat.id, id, 1).wrap(networkService)
                            .leftIfNull { MessageException }
                            .flatMap {
                                chatService.updateChat(chat.copy(lastMessage = it.lastOrNull()))
                                    .wrap(networkService)
                                    .map {
                                        message.id
                                    }
                            }
                    }
            }

}