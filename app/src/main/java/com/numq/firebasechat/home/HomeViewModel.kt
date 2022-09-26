package com.numq.firebasechat.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.SignOut
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.chat.CreateChat
import com.numq.firebasechat.chat.GetChats
import com.numq.firebasechat.user.GetUserById
import com.numq.firebasechat.user.UploadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserById: GetUserById,
    private val getChats: GetChats,
    private val createChat: CreateChat,
    private val uploadImage: UploadImage,
    private val signOut: SignOut
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun observeCurrentUser(userId: String) =
        getUserById.invoke(userId, onError) { user ->
            viewModelScope.launch {
                user.collect { user ->
                    _state.update {
                        it.copy(currentUser = user)
                    }
                }
            }
        }

    fun observeChats(userId: String, lastChatId: String?, limit: Long) =
        getChats.invoke(Triple(userId, lastChatId, limit), onError) { chats ->
            viewModelScope.launch {
                chats.collect { chat ->
                    _state.update {
                        it.copy(
                            chats = it.chats.filter { c -> c.id != chat.id }.plus(chat)
                                .sortedByDescending { c -> c.updatedAt }
                        )
                    }
                }
            }
        }

    fun loadMoreChats(userId: String, lastChatId: String?, limit: Long) =
        getChats.invoke(Triple(userId, lastChatId, limit), onError) { chats ->
            viewModelScope.launch {
                chats.toList().forEach { chat ->
                    _state.update {
                        it.copy(
                            chats = it.chats.filter { c -> c.id != chat.id }.plus(chat)
                                .sortedByDescending { c -> c.updatedAt }
                        )
                    }
                }
            }
        }

    fun createChat(userId: String, anotherId: String, onChatCreated: (Chat) -> Unit) =
        createChat.invoke(Pair(userId, anotherId), onError) { chat ->
            onChatCreated(chat)
        }

    fun uploadImage(id: String, bytes: ByteArray) =
        uploadImage.invoke(Pair(id, bytes), onError)

    fun signOut() = signOut.invoke(Unit, onError)

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