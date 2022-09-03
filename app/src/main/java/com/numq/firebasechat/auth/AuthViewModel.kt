package com.numq.firebasechat.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInByEmail: SignInByEmail,
    private val signUpByEmail: SignUpByEmail
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun changeAuthType(type: AuthType) = _state.update {
        it.copy(authType = type)
    }

    fun signIn(email: String, password: String) {
        _state.update {
            it.copy(isAuthenticating = true)
        }
        when (state.value.authType) {
            AuthType.EmailPassword -> {
                signInByEmail.invoke(Pair(email, password)) { data ->
                    data.fold(onError) {}
                }
            }
            else -> Unit
        }
    }

    fun signUp(name: String, email: String, password: String) {
        _state.update {
            it.copy(isAuthenticating = true)
        }
        when (state.value.authType) {
            AuthType.EmailPassword -> {
                signUpByEmail.invoke(Triple(name, email, password)) { data ->
                    data.fold(onError) {}
                }
            }
            else -> Unit
        }
    }

    fun cancelAuth() {
        viewModelScope.cancel()
        _state.update {
            it.copy(isAuthenticating = false)
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