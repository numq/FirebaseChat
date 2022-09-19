package com.numq.firebasechat.user

import arrow.core.left
import arrow.core.leftIfNull
import com.numq.firebasechat.wrapper.wrap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(
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
            .wrap()
            .leftIfNull { UserException }

    override suspend fun updateName(id: String, name: String) =
        userService.updateName(id, name)
            .wrap()
            .leftIfNull { UserException }

    override suspend fun updateEmail(id: String, email: String) =
        userService.updateEmail(id, email)
            .wrap()
            .leftIfNull { UserException }

    override suspend fun changePassword(id: String, password: String) =
        userService.changePassword(id, password)
            ?.wrap()
            ?.leftIfNull { UserException } ?: UserException.left()

    override suspend fun deleteUser(id: String) =
        userService.deleteUser(id).wrap().leftIfNull { UserException }

}