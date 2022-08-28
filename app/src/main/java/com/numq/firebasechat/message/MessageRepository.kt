package com.numq.firebasechat.message

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

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
    ): Either<Exception, Boolean>
    suspend fun readMessage(id: String): Either<Exception, Message>
    suspend fun deleteMessage(id: String): Either<Exception, String>
}