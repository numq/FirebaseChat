package com.numq.firebasechat.search

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.user.User

@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    currentUserId: String,
    vm: SearchViewModel = hiltViewModel(),
    onItemClick: (User) -> Unit,
    onCloseSearch: () -> Unit
) {

    BackHandler { onCloseSearch() }

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("SEARCH", state.toString())
    }

    DisposableEffect(vm) {
        onDispose {
            vm.cleanUpState()
        }
    }

    state.exception?.let {
        ShowError(scaffoldState, it, vm.cleanUpError)
    }

    val (searchQuery, setSearchQuery) = remember {
        mutableStateOf("")
    }

    LaunchedEffect(searchQuery) {
        vm.searchByQuery(currentUserId, searchQuery)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = searchQuery, onValueChange = {
            setSearchQuery(it)
        }, singleLine = true, placeholder = {
            Text("Type search query here...")
        }, trailingIcon = {
            if (searchQuery.isBlank()) {
                Icon(Icons.Rounded.Search, "search")
            } else {
                IconButton(onClick = { setSearchQuery("") }) {
                    Icon(Icons.Rounded.Clear, "clear query")
                }
            }
        }, modifier = Modifier.fillMaxWidth())
        LazyColumn(Modifier.weight(1f)) {
            items(state.searchResults) { item ->
                SearchItem(item) {
                    onCloseSearch()
                    onItemClick(it)
                }
            }
        }
    }
}