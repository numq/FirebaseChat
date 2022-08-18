package com.numq.firebasechat.user

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsersByQuery(query: String, limit: Long): Either<Exception, Flow<User>>
    suspend fun getUserById(id: String): Either<Exception, User>
    suspend fun updateUser(user: User): Either<Exception, User>
    suspend fun uploadImage(id: String, byteString: String): Either<Exception, User>
    suspend fun deleteUser(id: String): Either<Exception, String>
}