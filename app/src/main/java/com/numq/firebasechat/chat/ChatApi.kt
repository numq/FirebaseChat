package com.numq.firebasechat.chat

import com.google.android.gms.tasks.Task
import com.numq.firebasechat.message.Message
import kotlinx.coroutines.flow.Flow

interface ChatApi {
    fun getChats(userId: String, lastChatId: String?, limit: Long): Flow<Chat>
    fun getChatById(id: String): Task<Chat?>
    fun createChat(userId: String, anotherId: String): Task<Chat?>
    fun updateChat(chat: Chat): Task<Chat?>
    fun updateLastMessage(chatId: String, message: Message): Task<Chat?>
    fun deleteChat(id: String): Task<Void>
}