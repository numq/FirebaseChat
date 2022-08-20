package com.numq.firebasechat.user

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsersByQuery(query: String, limit: Long): Either<Exception, Flow<User>>
    suspend fun getUserById(id: String): Either<Exception, Flow<User>>
    suspend fun uploadImage(id: String, bytes: ByteArray): Either<Exception, User>
    suspend fun updateName(id: String, name: String): Either<Exception, User>
    suspend fun updateEmail(id: String, email: String): Either<Exception, User>
    suspend fun changePassword(id: String, password: String): Either<Exception, User>
    suspend fun deleteUser(id: String): Either<Exception, String>
}