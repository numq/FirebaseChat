package com.numq.firebasechat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.GetAuthenticationId
import com.numq.firebasechat.auth.SignOut
import com.numq.firebasechat.chat.CreateChat
import com.numq.firebasechat.chat.GetChatById
import com.numq.firebasechat.chat.GetChats
import com.numq.firebasechat.user.GetUserById
import com.numq.firebasechat.user.UpdateLastActiveChat
import com.numq.firebasechat.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val DEFAULT_LIMIT = 10L

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

    private fun observeChats(userId: String) =
        getChats.invoke(Pair(userId, DEFAULT_LIMIT)) { data ->
            data.fold(onError) { chats ->
                viewModelScope.launch {
                    chats.collect { chat ->
                        _state.update {
                            it.copy(chats = it.chats.plus(chat).distinct())
                        }
                    }
                }
            }
        }

    init {
        observeCurrentUser { user ->
            user.lastActiveChatId?.let { observeActiveChat(it) }
            observeChats(user.id)
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