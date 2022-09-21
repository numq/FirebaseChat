package com.numq.firebasechat.di

import com.numq.firebasechat.auth.AuthApi
import com.numq.firebasechat.auth.AuthService
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.chat.ChatService
import com.numq.firebasechat.message.MessageApi
import com.numq.firebasechat.message.MessageService
import com.numq.firebasechat.network.NetworkApi
import com.numq.firebasechat.network.NetworkService
import com.numq.firebasechat.user.UserApi
import com.numq.firebasechat.user.UserService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindNetworkApi(service: NetworkService): NetworkApi

    @Binds
    @Singleton
    abstract fun bindAuthApi(service: AuthService): AuthApi

    @Binds
    @Singleton
    abstract fun bindChatApi(service: ChatService): ChatApi

    @Binds
    @Singleton
    abstract fun bindMessageApi(service: MessageService): MessageApi

    @Binds
    @Singleton
    abstract fun bindUserApi(service: UserService): UserApi

}