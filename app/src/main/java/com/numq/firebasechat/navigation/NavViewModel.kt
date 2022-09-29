package com.numq.firebasechat.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.AuthenticationState
import com.numq.firebasechat.auth.GetAuthenticationState
import com.numq.firebasechat.network.GetNetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    getAuthenticationState: GetAuthenticationState,
    private val getNetworkStatus: GetNetworkStatus
) : ViewModel() {

    private val _state = MutableStateFlow(NavState())
    val state = _state.asStateFlow()

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

    init {
        getAuthenticationState.invoke(Unit, onError) {
            viewModelScope.launch {
                it.collect { authState ->
                    when (authState) {
                        is AuthenticationState.Authenticating ->
                            _state.update {
                                it.copy(authenticating = true)
                            }
                        is AuthenticationState.Authenticated ->
                            _state.update {
                                it.copy(userId = authState.userId, authenticating = false)
                            }
                        is AuthenticationState.Unauthenticated ->
                            _state.update {
                                it.copy(authenticating = false)
                            }
                        is AuthenticationState.Failure ->
                            _state.update {
                                it.copy(exception = authState.exception, authenticating = false)
                            }
                    }
                }
            }
        }
    }

    fun getNetworkStatus() = getNetworkStatus.invoke(Unit, onError) {
        viewModelScope.launch {
            it.collect { status ->
                _state.update {
                    it.copy(status = status)
                }
            }
        }
    }

}