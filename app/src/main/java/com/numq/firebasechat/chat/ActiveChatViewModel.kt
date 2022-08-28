package com.numq.firebasechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.message.GetLatestMessages
import com.numq.firebasechat.message.GetMessages
import com.numq.firebasechat.message.ReadMessage
import com.numq.firebasechat.message.SendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveChatViewModel @Inject constructor(
    private val getLatestMessages: GetLatestMessages,
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage,
    private val readMessage: ReadMessage
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun observeLastMessages(chatId: String, limit: Long) =
        getLatestMessages.invoke(Pair(chatId, limit)) { data ->
            data.fold(onError) { messages ->
                viewModelScope.launch {
                    messages.collect { msg ->
                        if (msg.id !in state.value.messages.map { m -> m.id }) {
                            _state.update {
                                it.copy(
                                    messages = listOf(msg).plus(it.messages)
                                        .sortedByDescending { m -> m.sentAt }
                                )
                            }
                        }
                    }
                }
            }
        }

    fun loadMore(chatId: String, lastMessageId: String, limit: Long) =
        getMessages.invoke(Triple(chatId, lastMessageId, limit)) { data ->
            data.fold(onError) { messages ->
                _state.update {
                    it.copy(
                        messages = it.messages.plus(messages.filter { msg -> msg.id !in it.messages.map { m -> m.id } }
                            .sortedByDescending { m -> m.sentAt })
                    )
                }
            }
        }

    fun sendMessage(chatId: String, userId: String, text: String, onMessageSent: () -> Unit) =
        sendMessage.invoke(Triple(chatId, userId, text)) { data ->
            data.fold(onError) {
                if (it) onMessageSent()
            }
        }

    fun readMessage(id: String) = readMessage.invoke(id) { data ->
        data.fold(onError) { updatedMessage ->
            _state.update {
                it.copy(messages = it.messages.map { msg -> if (msg.id == updatedMessage.id) updatedMessage else msg })
            }
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