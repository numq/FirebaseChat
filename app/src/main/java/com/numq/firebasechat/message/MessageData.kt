package com.numq.firebasechat.message

import arrow.core.leftIfNull
import com.numq.firebasechat.chat.ChatService
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
            .map { document ->
                if (chatService.getChatById(chatId).addOnSuccessListener { chat ->
                        chat?.let {
                            chatService.updateChat(chat.copy(lastMessage = document.message))
                        }
                    }.isSuccessful) true else null
            }.leftIfNull { MessageException }

    override suspend fun readMessage(id: String) =
        messageService.readMessage(id)
            .wrap()
            .map {
                it.message?.let { message ->
                    if (chatService.getChatById(message.chatId).addOnSuccessListener { chat ->
                            chat?.let {
                                if (chat.lastMessage?.id == message.id) {
                                    chatService.updateChat(chat.copy(lastMessage = message))
                                }
                            }
                        }.isSuccessful) message else null
                }
            }.leftIfNull { MessageException }

    override suspend fun deleteMessage(id: String) =
        messageService.deleteMessage(id)
            .wrap()
            .map {
                if (it?.addOnSuccessListener { document ->
                        document.message?.let { message ->
                            chatService.getChatById(message.chatId)
                                .addOnSuccessListener { chat ->
                                    chat?.let {
                                        chatService.updateChat(chat.copy(lastMessage = message))
                                    }
                                }
                        }
                    }?.isSuccessful == true) id else null
            }.leftIfNull { MessageException }
}