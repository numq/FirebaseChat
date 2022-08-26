package com.numq.firebasechat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.SignOut
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.chat.CreateChat
import com.numq.firebasechat.chat.GetChats
import com.numq.firebasechat.chat.GetLatestChats
import com.numq.firebasechat.user.GetUserById
import com.numq.firebasechat.user.UploadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserById: GetUserById,
    private val getLatestChats: GetLatestChats,
    private val getChats: GetChats,
    private val createChat: CreateChat,
    private val uploadImage: UploadImage,
    private val signOut: SignOut
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun observeCurrentUser(userId: String) =
        getUserById.invoke(userId) { data ->
            data.fold(onError) { user ->
                viewModelScope.launch {
                    user.collect { user ->
                        _state.update {
                            it.copy(currentUser = user)
                        }
                    }
                }
            }
        }

    fun observeChats(userId: String, limit: Long) =
        getLatestChats.invoke(Pair(userId, limit)) { data ->
            data.fold(onError) { chats ->
                viewModelScope.launch {
                    chats.collect { chat ->
                        if (chat !in state.value.chats) {
                            _state.update {
                                it.copy(
                                    chats = it.chats.plus(chat)
                                )
                            }
                        }
                    }
                }
            }
        }

    fun loadMore(userId: String, lastChatId: String, limit: Long) =
        getChats.invoke(Triple(userId, lastChatId, limit)) { data ->
            data.fold(onError) { chats ->
                _state.update {
                    it.copy(
                        chats = it.chats.plus(chats.filter { chat -> chat !in it.chats })
                    )
                }
            }
        }

    fun updateActiveChat(chat: Chat) = _state.update {
        it.copy(activeChat = chat)
    }

    fun createChat(userId: String, anotherId: String) =
        createChat.invoke(Pair(userId, anotherId)) { data ->
            data.fold(onError) { chat ->
                _state.update {
                    it.copy(activeChat = chat)
                }
            }
        }

    fun uploadImage(id: String, bytes: ByteArray) =
        uploadImage.invoke(Pair(id, bytes)) { data ->
            data.fold(onError) {}
        }

    fun signOut() = signOut.invoke(Unit) { data ->
        data.fold(onError) {}
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