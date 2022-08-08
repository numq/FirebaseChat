package com.numq.firebasechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.message.GetMessages
import com.numq.firebasechat.message.SendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveChatViewModel @Inject constructor(
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val DEFAULT_LIMIT = 10L

    fun observeMessages(chatId: String) {
        getMessages.invoke(Pair(chatId, DEFAULT_LIMIT)) { data ->
            data.fold(onError) { messages ->
                viewModelScope.launch {
                    messages.collect { message ->
                        _state.update {
                            it.copy(messages = it.messages.plus(message).distinct())
                        }
                    }
                }
            }
        }
    }

    fun sendMessage(chatId: String, userId: String, text: String, onMessageSent: () -> Unit) =
        sendMessage.invoke(Triple(chatId, userId, text)) { data ->
            data.fold(onError) {
                if (it) onMessageSent()
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