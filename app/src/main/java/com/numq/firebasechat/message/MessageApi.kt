package com.numq.firebasechat.message

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface MessageApi {
    fun getMessages(chatId: String, limit: Long): Task<QuerySnapshot>
    fun getMessageById(id: String): Task<DocumentSnapshot>
    fun createMessage(chatId: String, userId: String, text: String): Task<Void>
    fun updateMessage(id: String, text: String): Task<Void>
    fun deleteMessage(id: String): Task<Void>
}