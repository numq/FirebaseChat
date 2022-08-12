package com.numq.firebasechat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.numq.firebasechat.auth.GetAuthenticationId
import com.numq.firebasechat.auth.SignOut
import com.numq.firebasechat.chat.*
import com.numq.firebasechat.user.GetUserById
import com.numq.firebasechat.user.UpdateLastActiveChat
import com.numq.firebasechat.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAuthenticationId: GetAuthenticationId,
    private val getUserById: GetUserById,
    private val getChatById: GetChatById,
    private val getChats: GetChats,
    private val updateLastActiveChat: UpdateLastActiveChat,
    private val createChat: CreateChat,
    private val signOut: SignOut
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private fun observeCurrentUser(onUser: (User) -> Unit) =
        getAuthenticationId.invoke(Unit) { data ->
            data.fold(onError) {
                viewModelScope.launch {
                    it.collect { id ->
                        id?.let {
                            getUserById.invoke(id) { data ->
                                data.fold(onError) { user ->
                                    _state.update {
                                        it.copy(currentUser = user)
                                    }
                                    onUser(user)
                                }
                            }
                        } ?: _state.update {
                            it.copy(currentUser = null)
                        }
                    }
                }
            }
        }

    private fun observeActiveChat(chatId: String) = getChatById.invoke(chatId) { data ->
        data.fold(onError) { chat ->
            _state.update {
                it.copy(activeChat = chat)
            }
        }
    }

    private val defaultLimit = 20L

    fun createPagingSource(userId: String) = Pager(PagingConfig(defaultLimit.toInt())) {
        val result = mutableListOf<Chat>()
        ChatPagingSource(defaultLimit.toInt()) { offset, limit ->
            getChats.invoke(Triple(userId, offset.toLong(), limit.toLong())) { data ->
                data.fold(onError) { chats ->
                    viewModelScope.launch {
                        result.apply {
                            clear()
                            addAll(chats.toList())
                        }
                    }
                }
            }
            result
        }
    }.flow

    init {
        observeCurrentUser { user ->
            user.lastActiveChatId?.let { observeActiveChat(it) }
        }
    }

    fun updateLastActiveChat(userId: String, chatId: String) =
        updateLastActiveChat.invoke(Pair(userId, chatId)) { data ->
            data.fold(onError) { }
        }

    fun createChat(userId: String, anotherId: String) =
        createChat.invoke(Pair(userId, anotherId)) { data ->
            data.fold(onError) { chat ->
                _state.update {
                    it.copy(activeChat = chat)
                }
            }
        }

    fun signOut(action: () -> Unit) = signOut.invoke(Unit) { data ->
        data.fold(onError) {
            action()
        }
    }

    private val onError: (Exception) -> Unit = { exception ->
        _state.update {
            it.copy(exception = exception)
        }
    }

    val cleanUpError: () -> Unit = {
        _state.update {
            it.copy(exception = null)
        }
    }

}