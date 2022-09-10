package com.numq.firebasechat.message

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun getLatestMessages(chatId: String, limit: Long): Flow<Message>
    fun getMessages(chatId: String, lastMessageId: String, limit: Long): Task<List<Message>>
    fun createMessage(chatId: String, userId: String, text: String): Task<Message?>
    fun readMessage(id: String): Task<Message?>
    fun deleteMessage(id: String): Task<Message?>?
}