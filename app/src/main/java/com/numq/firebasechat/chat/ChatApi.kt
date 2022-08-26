package com.numq.firebasechat.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.numq.firebasechat.message.Message
import kotlinx.coroutines.flow.Flow

interface ChatApi {
    fun getLatestChats(userId: String, limit: Long): Flow<DocumentSnapshot>
    fun getChats(userId: String, lastChatId: String, limit: Long): Task<QuerySnapshot>
    fun getChatById(id: String): Task<DocumentSnapshot>
    fun createChat(userId: String, anotherId: String): Task<DocumentSnapshot>
    fun updateChat(chat: Chat): Task<DocumentSnapshot>
    fun updateLastMessage(chatId: String, message: Message): Task<DocumentSnapshot>
    fun deleteChat(id: String): Task<Void>
}