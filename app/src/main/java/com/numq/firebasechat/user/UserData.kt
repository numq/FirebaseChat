package com.numq.firebasechat.user

import arrow.core.left
import arrow.core.leftIfNull
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkException
import com.numq.firebasechat.wrapper.wrap
import com.numq.firebasechat.wrapper.wrapIf
import javax.inject.Inject

class UserData @Inject constructor(
    private val networkService: NetworkApi,
    private val userService: UserApi
) : UserRepository {

    override suspend fun getUsersByQuery(query: String, limit: Long) =
        userService.getUsersByQuery(query, limit)
            .wrap()
            .leftIfNull { UserException }

    override suspend fun getUserById(id: String) =
        userService.getUserById(id)
            .wrap()
            .leftIfNull { UserException }

    override suspend fun uploadImage(id: String, bytes: ByteArray) =
        userService.uploadImage(id, bytes)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { UserException }

    override suspend fun updateName(id: String, name: String) =
        userService.updateName(id, name)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { UserException }

    override suspend fun updateEmail(id: String, email: String) =
        userService.updateEmail(id, email)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { UserException }

    override suspend fun changePassword(id: String, password: String) =
        userService.changePassword(id, password)
            ?.wrapIf(networkService.isAvailable, NetworkException.Default)
            ?.leftIfNull { UserException } ?: UserException.left()

    override suspend fun deleteUser(id: String) =
        userService.deleteUser(id)
            .wrapIf(networkService.isAvailable, NetworkException.Default)
            .leftIfNull { UserException }

}