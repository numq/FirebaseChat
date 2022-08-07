package com.numq.firebasechat.user

import arrow.core.leftIfNull
import com.numq.firebasechat.mapper.user
import com.numq.firebasechat.wrapper.wrap
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(
    private val userService: UserService
) : UserRepository {

    override suspend fun getUsersByQuery(query: String, limit: Long) =
        userService.getUsersByQuery(query, limit)
            .wrap()
            .map { it.asFlow().mapNotNull { document -> document.user } }
            .leftIfNull { UserException }

    override suspend fun getUserById(id: String) =
        userService.getUserById(id)
            .wrap()
            .map { it.user }
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