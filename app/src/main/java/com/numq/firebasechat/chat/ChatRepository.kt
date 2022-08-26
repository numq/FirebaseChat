package com.numq.firebasechat.chat

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getLatestChats(userId: String, limit: Long): Either<Exception, Flow<Chat>>
    suspend fun getChats(
        userId: String,
        lastChatId: String,
        limit: Long
    ): Either<Exception, List<Chat>>
    suspend fun getChatById(id: String): Either<Exception, Chat>
    suspend fun createChat(userId: String, anotherId: String): Either<Exception, Chat>
    suspend fun updateChat(chat: Chat): Either<Exception, Chat>
    suspend fun deleteChat(id: String): Either<Exception, String>
}