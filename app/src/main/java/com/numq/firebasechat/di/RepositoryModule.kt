package com.numq.firebasechat.di

import com.numq.firebasechat.auth.AuthRepository
import com.numq.firebasechat.chat.ChatRepository
import com.numq.firebasechat.message.MessageRepository
import com.numq.firebasechat.network.NetworkRepository
import com.numq.firebasechat.user.UserData
import com.numq.firebasechat.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(repository: NetworkRepository.Implementation): NetworkRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: AuthRepository.Implementation): AuthRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(repository: ChatRepository.Implementation): ChatRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(repository: MessageRepository.Implementation): MessageRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(data: UserData): UserRepository

}