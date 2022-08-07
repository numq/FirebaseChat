package com.numq.firebasechat.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface UserApi {
    fun getUsersByQuery(query: String, limit: Long): Task<QuerySnapshot>
    fun getUserById(id: String): Task<DocumentSnapshot>
    fun createUser(id: String, email: String): Task<Void>
    fun updateUserActivity(id: String, state: Boolean): Task<Void>
    fun updateUser(user: User): Task<Void>
    fun deleteUser(id: String): Task<Void>
}