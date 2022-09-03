package com.numq.firebasechat.user

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface UserApi {
    fun getUsersByQuery(query: String, limit: Long): Flow<DocumentSnapshot>
    fun getUserById(id: String): Flow<DocumentSnapshot>
    fun createUser(id: String, name: String?, email: String): Task<Void>
    fun updateUserActivity(id: String, state: Boolean): Task<Void>
    fun uploadImage(id: String, bytes: ByteArray): Task<DocumentSnapshot>
    fun updateName(id: String, name: String): Task<DocumentSnapshot>
    fun updateEmail(id: String, email: String): Task<DocumentSnapshot>
    fun changePassword(id: String, password: String): Task<DocumentSnapshot>?
    fun deleteUser(id: String): Task<Void>
}