package com.numq.firebasechat.message

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun getLatestMessages(chatId: String, limit: Long): Flow<DocumentSnapshot>
    fun getMessages(chatId: String, lastMessageId: String, limit: Long): Task<QuerySnapshot>
    fun getMessageById(id: String): Task<DocumentSnapshot>
    fun createMessage(chatId: String, userId: String, text: String): Task<DocumentSnapshot>
    fun readMessage(id: String): Task<DocumentSnapshot>
    fun deleteMessage(id: String): Task<Void>
}