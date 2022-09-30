package com.numq.firebasechat.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.auth.GetAuthenticationState
import com.numq.firebasechat.network.GetNetworkStatus
import com.numq.firebasechat.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    getNetworkStatus: GetNetworkStatus,
    getAuthenticationState: GetAuthenticationState
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
        getNetworkStatus.invoke(Unit, onError) {
            viewModelScope.launch {
                it.collect { status ->
                    if (status is NetworkStatus.Available) {
                        getAuthenticationState.invoke(Unit, onError) {
                            viewModelScope.launch {
                                it.collect { authState ->
                                    _state.update {
                                        it.copy(status = status, authenticationState = authState)
                                    }
                                }
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(status = status)
                        }
                    }
                }
            }
        }
    }

}