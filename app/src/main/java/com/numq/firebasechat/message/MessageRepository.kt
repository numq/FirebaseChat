package com.numq.firebasechat.message

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

interface MessageRepository {

    suspend fun getLatestMessages(
        chatId: String,
        limit: Long
    ): Either<Exception, Flow<Message>>

    suspend fun getMessages(
        chatId: String,
        lastMessageId: String,
        limit: Long
    ): Either<Exception, List<Message>>

    suspend fun createMessage(
        chatId: String,
        userId: String,
        text: String
    ): Either<Exception, Message>

    suspend fun readMessage(id: String): Either<Exception, Message>
    suspend fun deleteMessage(id: String): Either<Exception, String>

    class Implementation @Inject constructor(
        private val networkService: NetworkApi,
        private val chatService: ChatApi,
        private val messageService: MessageApi
    ) : MessageRepository {

        override suspend fun getLatestMessages(chatId: String, limit: Long) =
            messageService.getLatestMessages(chatId, limit)
                .wrap()
                .leftIfNull { MessageException.Default }

        override suspend fun getMessages(chatId: String, lastMessageId: String, limit: Long) =
            messageService.getMessages(chatId, lastMessageId, limit)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { MessageException.Default }

        override suspend fun createMessage(chatId: String, userId: String, text: String) =
            messageService.createMessage(chatId, userId, text)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { MessageException.Default }
                .flatMap { message ->
                    chatService.getChatById(message.chatId)
                        .wrapIf(networkService.isAvailable, NetworkException.Default)
                        .leftIfNull { MessageException.Default }
                        .map { it.lastOrNull() }
                        .leftIfNull { MessageException.Default }
                        .flatMap { chat ->
                            chatService.updateChat(chat.copy(lastMessage = message))
                                .wrapIf(networkService.isAvailable, NetworkException.Default)
                                .leftIfNull { MessageException.Default }
                        }
                        .map { message }
                }

        override suspend fun readMessage(id: String) =
            messageService.readMessage(id)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { MessageException.Default }
                .flatMap { message ->
                    chatService.getChatById(message.chatId)
                        .wrapIf(networkService.isAvailable, NetworkException.Default)
                        .leftIfNull { MessageException.Default }
                        .map { it.lastOrNull() }
                        .leftIfNull { MessageException.Default }
                        .flatMap { chat ->
                            if (chat.lastMessage?.id == message.id) {
                                chatService.updateChat(chat.copy(lastMessage = message))
                                    .wrapIf(networkService.isAvailable, NetworkException.Default)
                                    .leftIfNull { MessageException.Default }
                            } else {
                                chat.wrap()
                            }
                        }.map { message }
                }

        override suspend fun deleteMessage(id: String) =
            messageService.deleteMessage(id)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { MessageException.Default }
                .flatMap { message ->
                    chatService.getChatById(message.chatId)
                        .wrapIf(networkService.isAvailable, NetworkException.Default)
                        .leftIfNull { MessageException.Default }
                        .map { it.lastOrNull() }
                        .leftIfNull { MessageException.Default }
                        .flatMap { chat ->
                            messageService.getMessages(chat.id, id, 1)
                                .wrapIf(networkService.isAvailable, NetworkException.Default)
                                .leftIfNull { MessageException.Default }
                                .flatMap {
                                    chatService.updateChat(chat.copy(lastMessage = it.lastOrNull()))
                                        .wrapIf(
                                            networkService.isAvailable,
                                            NetworkException.Default
                                        )
                                        .map { message.id }
                                }
                        }
                }

    }

}