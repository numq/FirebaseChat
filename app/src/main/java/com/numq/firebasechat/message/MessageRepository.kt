package com.numq.firebasechat.message

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(chatId: String, limit: Long): Either<Exception, Flow<Message>>
    suspend fun getMessageById(id: String): Either<Exception, Message>
    suspend fun createMessage(chatId: String, userId: String, text: String): Either<Exception, Boolean>
    suspend fun updateMessage(id: String, text: String): Either<Exception, Boolean>
    suspend fun deleteMessage(id: String): Either<Exception, String>
}