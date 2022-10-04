package com.numq.firebasechat.di

import com.numq.firebasechat.auth.AuthApi
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.message.MessageApi
import com.numq.firebasechat.network.NetworkApi
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
    abstract fun bindNetworkApi(api: NetworkApi.Implementation): NetworkApi

    @Binds
    @Singleton
    abstract fun bindAuthApi(api: AuthApi.Implementation): AuthApi

    @Binds
    @Singleton
    abstract fun bindChatApi(api: ChatApi.Implementation): ChatApi

    @Binds
    @Singleton
    abstract fun bindMessageApi(api: MessageApi.Implementation): MessageApi

    @Binds
    @Singleton
    abstract fun bindUserApi(service: UserService): UserApi

}