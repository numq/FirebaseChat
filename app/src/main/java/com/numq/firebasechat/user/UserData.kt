package com.numq.firebasechat.user

import arrow.core.left
import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.user
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(
    private val userService: UserApi
) : UserRepository {

    override suspend fun getUsersByQuery(query: String, limit: Long) =
        userService.getUsersByQuery(query, limit)
            .mapNotNull { it.user }
            .wrap()
            .leftIfNull { UserException }

    override suspend fun getUserById(id: String) =
        userService.getUserById(id)
            .mapNotNull { it.user }
            .wrap()
            .leftIfNull { UserException }

    override suspend fun uploadImage(id: String, bytes: ByteArray) =
        userService.uploadImage(id, bytes)
            .wrap()
            .map { it.user }
            .leftIfNull { UserException }

    override suspend fun updateName(id: String, name: String) =
        userService.updateName(id, name)
            .wrap()
            .map { it.user }
            .leftIfNull { UserException }

    override suspend fun updateEmail(id: String, email: String) =
        userService.updateEmail(id, email)
            .wrap()
            .map { it.user }
            .leftIfNull { UserException }

    override suspend fun changePassword(id: String, password: String) =
        userService.changePassword(id, password)
            ?.wrap()
            ?.map { it.user }
            ?.leftIfNull { UserException } ?: UserException.left()

    override suspend fun deleteUser(id: String) =
        userService.deleteUser(id)
            .wrap()
            .map { id }

}