package com.numq.firebasechat.user

import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.chat
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
            .wrap()
            .map { it.user }
            .leftIfNull { UserException }

    override suspend fun updateLastActiveChat(
        userId: String,
        chatId: String
    ) = userService.updateLastActiveChat(userId, chatId)
        .wrap()
        .map { it.chat }
        .leftIfNull { UserException }

    override suspend fun updateUser(user: User) =
        userService.updateUser(user)
            .wrap()
            .map { user }

    override suspend fun deleteUser(id: String) =
        userService.deleteUser(id)
            .wrap()
            .map { id }

}