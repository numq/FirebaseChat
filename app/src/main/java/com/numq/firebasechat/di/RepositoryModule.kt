package com.numq.firebasechat.di

import com.numq.firebasechat.auth.AuthData
import com.numq.firebasechat.auth.AuthRepository
import com.numq.firebasechat.chat.ChatData
import com.numq.firebasechat.chat.ChatRepository
import com.numq.firebasechat.message.MessageData
import com.numq.firebasechat.message.MessageRepository
import com.numq.firebasechat.user.UserData
import com.numq.firebasechat.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(repository: AuthData): AuthRepository

    @Binds
    abstract fun bindChatRepository(repository: ChatData): ChatRepository

    @Binds
    abstract fun bindMessageRepository(repository: MessageData): MessageRepository

    @Binds
    abstract fun bindUserRepository(repository: UserData): UserRepository

}