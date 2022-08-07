package com.numq.firebasechat.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface ChatApi {
    fun getChats(userId: String, limit: Long): Task<QuerySnapshot>
    fun getChatById(id: String): Task<DocumentSnapshot>
    fun createChat(userId: String, anotherId: String): Task<DocumentSnapshot>
    fun updateChat(chat: Chat): Task<DocumentSnapshot>
    fun deleteChat(id: String): Task<Void>
}