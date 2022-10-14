package com.numq.firebasechat.user

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository {

    suspend fun getUserById(id: String): Either<Exception, Flow<User>>
    suspend fun uploadImage(id: String, bytes: ByteArray): Either<Exception, User>
    suspend fun updateName(id: String, name: String): Either<Exception, User>
    suspend fun updateEmail(id: String, email: String): Either<Exception, User>
    suspend fun changePassword(id: String, password: String): Either<Exception, User>
    suspend fun deleteUser(id: String): Either<Exception, String>

    class Implementation @Inject constructor(
        private val networkService: NetworkApi,
        private val userService: UserApi
    ) : UserRepository {

        override suspend fun getUserById(id: String) =
            userService.getUserById(id)
                .wrap()
                .leftIfNull { UserException.Default }

        override suspend fun uploadImage(id: String, bytes: ByteArray) =
            userService.uploadImage(id, bytes)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { UserException.Default }

        override suspend fun updateName(id: String, name: String) =
            userService.updateName(id, name)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { UserException.Default }

        override suspend fun updateEmail(id: String, email: String) =
            userService.updateEmail(id, email)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { UserException.Default }

        override suspend fun changePassword(id: String, password: String) =
            userService.changePassword(id, password)
                ?.wrapIf(networkService.isAvailable, NetworkException.Default)
                ?.leftIfNull { UserException.Default } ?: UserException.Default.left()

        override suspend fun deleteUser(id: String) =
            userService.deleteUser(id)
                .wrapIf(networkService.isAvailable, NetworkException.Default)
                .leftIfNull { UserException.Default }

    }

}