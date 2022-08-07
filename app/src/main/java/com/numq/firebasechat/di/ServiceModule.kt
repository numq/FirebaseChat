package com.numq.firebasechat.di

import com.numq.firebasechat.auth.AuthApi
import com.numq.firebasechat.auth.AuthService
import com.numq.firebasechat.chat.ChatApi
import com.numq.firebasechat.chat.ChatService
import com.numq.firebasechat.message.MessageApi
import com.numq.firebasechat.message.MessageService
import com.numq.firebasechat.user.UserApi
import com.numq.firebasechat.user.UserService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindAuthApi(api: AuthService): AuthApi

    @Binds
    abstract fun bindChatApi(api: ChatService): ChatApi

    @Binds
    abstract fun bindMessageApi(api: MessageService): MessageApi

    @Binds
    abstract fun bindUserApi(api: UserService): UserApi

}