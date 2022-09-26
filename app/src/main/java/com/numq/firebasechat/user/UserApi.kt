package com.numq.firebasechat.user

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface UserApi {
    fun getUsersByQuery(query: String, limit: Long): Flow<User>
    fun getUserById(id: String): Flow<User>?
    fun createUser(id: String, name: String, email: String): Task<User>
    fun updateUserActivity(id: String, state: Boolean): Task<Void>
    fun uploadImage(id: String, bytes: ByteArray): Task<User?>
    fun updateName(id: String, name: String): Task<User?>
    fun updateEmail(id: String, email: String): Task<User?>
    fun changePassword(id: String, password: String): Task<User?>?
    fun deleteUser(id: String): Task<String>
}