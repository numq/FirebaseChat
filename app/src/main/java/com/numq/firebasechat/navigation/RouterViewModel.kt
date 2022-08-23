package com.numq.firebasechat.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.GetAuthenticationId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    getAuthenticationId: GetAuthenticationId
) : ViewModel() {

    private val _state = MutableStateFlow(RouterState())
    val state = _state.asStateFlow()

    init {
        getAuthenticationId.invoke(Unit) { data ->
            data.fold(onError) {
                viewModelScope.launch {
                    it.collect { id ->
                        _state.update {
                            it.copy(userId = id, isLoading = false)
                        }
                    }
                }
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