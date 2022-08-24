package com.numq.firebasechat.message

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(
        chatId: String,
        skip: Long,
        limit: Long
    ): Either<Exception, Flow<Message>>

    suspend fun getMessageById(id: String): Either<Exception, Message>
    suspend fun createMessage(
        chatId: String,
        userId: String,
        text: String
    ): Either<Exception, Boolean>

    suspend fun readMessage(id: String): Either<Exception, Unit>
    suspend fun deleteMessage(id: String): Either<Exception, String>
}