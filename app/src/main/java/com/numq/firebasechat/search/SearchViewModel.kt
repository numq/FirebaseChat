package com.numq.firebasechat.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.user.GetUsersByQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUsersByQuery: GetUsersByQuery
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
            Log.e("debounce", "fired")
        }
    }

    private val DEFAULT_LIMIT = 10L

    fun searchByQuery(query: String) {
        debounce {
            getUsersByQuery.invoke(Pair(query, DEFAULT_LIMIT)) { data ->
                data.fold(onError) { users ->
                    viewModelScope.launch {
                        _state.update {
                            SearchState(searchResults = users.toList())
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