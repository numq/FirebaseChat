package com.numq.firebasechat.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val findUsersByQuery: FindUsersByQuery
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var debounceJob: Job? = null

    private fun debounce(
        timeout: Long = 500L,
        action: () -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(timeout)
            action()
        }
    }

    fun searchByQuery(currentUserId: String, query: String) {
        debounce {
            findUsersByQuery.invoke(query, onError) { users ->
                viewModelScope.launch {
                    users.collect { user ->
                        if (user.id != currentUserId) {
                            _state.update {
                                SearchState(searchResults = it.searchResults.plus(user).distinct())
                            }
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

    val cleanUpState: () -> Unit = {
        _state.update {
            it.copy(searchResults = emptyList())
        }
    }

}