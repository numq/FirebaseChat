package com.numq.firebasechat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserById: GetUserById,
    private val uploadImage: UploadImage,
    private val updateName: UpdateName,
    private val updateEmail: UpdateEmail,
    private val changePassword: ChangePassword
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    fun getUserById(id: String) = getUserById.invoke(id, onError) { user ->
        viewModelScope.launch {
            user.collect { user ->
                _state.update {
                    it.copy(currentUser = user)
                }
            }
        }

    }

    fun uploadImage(id: String, byteArray: ByteArray) =
        uploadImage.invoke(Pair(id, byteArray), onError) { user ->
            _state.update { it.copy(currentUser = user) }

        }

    fun updateName(id: String, name: String) = updateName.invoke(Pair(id, name), onError) { user ->
        _state.update { it.copy(currentUser = user) }

    }

    fun updateEmail(id: String, email: String) =
        updateEmail.invoke(Pair(id, email), onError) { user ->
            _state.update { it.copy(currentUser = user) }

        }

    fun changePassword(id: String, password: String) =
        changePassword.invoke(Pair(id, password), onError) { user ->
            _state.update { it.copy(currentUser = user) }

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