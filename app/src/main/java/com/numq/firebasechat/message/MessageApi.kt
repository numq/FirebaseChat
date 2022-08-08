package com.numq.firebasechat.message

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun getMessages(chatId: String, limit: Long): Flow<DocumentSnapshot>
    fun getMessageById(id: String): Task<DocumentSnapshot>
    fun createMessage(chatId: String, userId: String, text: String): Task<DocumentSnapshot>
    fun updateMessage(id: String, text: String): Task<Void>
    fun deleteMessage(id: String): Task<Void>
}