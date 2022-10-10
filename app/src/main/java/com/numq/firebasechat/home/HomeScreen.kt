package com.numq.firebasechat.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.chat.ChatListItem
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.home.dialog.SignOutDialog
import com.numq.firebasechat.home.drawer.Drawer
import com.numq.firebasechat.home.drawer.DrawerArticle
import com.numq.firebasechat.list.isReachedTheEnd
import com.numq.firebasechat.search.SearchScreen
import com.numq.firebasechat.user.User

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    userId: String,
    vm: HomeViewModel = hiltViewModel(),
    navigateToChat: (String) -> Unit,
    navigateToSettings: () -> Unit
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("HOME", state.toString())
        vm.observeCurrentUser(userId)
        vm.observeChats(userId, null, 15L)
    }

    state.exception?.let {
        ShowError(scaffoldState, it, vm.cleanUpError)
    }

    val chatState = rememberLazyListState()

    chatState.isReachedTheEnd(3) {
        state.chats.lastOrNull()?.let {
            vm.loadMoreChats(userId, it.id, 15L)
        }
    }

    state.currentUser?.let { user ->
        BuildHome(
            scaffoldState,
            currentUser = user,
            chatState = chatState,
            chats = state.chats,
            createChat = {
                vm.createChat(user.id, it.id) { chat ->
                    navigateToChat(chat.id)
                }
            },
            openChat = {
                navigateToChat(it)
            },
            uploadImage = {
                vm.uploadImage(user.id, it)
            },
            openSettings = {
                navigateToSettings()
            },
            signOut = {
                vm.signOut()
            })
    }

}

@Composable
fun BuildHome(
    scaffoldState: ScaffoldState,
    currentUser: User,
    chatState: LazyListState,
    chats: List<Chat>,
    createChat: (User) -> Unit,
    openChat: (String) -> Unit,
    uploadImage: (ByteArray) -> Unit,
    openSettings: (String) -> Unit,
    signOut: () -> Unit
) {

    val (signOutVisible, setSignOutVisible) = remember {
        mutableStateOf(false)
    }

    val (isSearching, setIsSearching) = remember {
        mutableStateOf(false)
    }

    val searchModeModifier =
        if (isSearching) Modifier
            .background(Color.Black)
            .alpha(.5f)
            .clickable { setIsSearching(false) }
        else Modifier

    Drawer(currentUser, onUploadImage = uploadImage, onDrawerArticleClick = {
        when (it) {
            is DrawerArticle.Settings -> openSettings(currentUser.id)
            is DrawerArticle.SignOut -> setSignOutVisible(true)
        }
    }) { drawerState, openDrawer, closeDrawer ->
        BackHandler(drawerState.isOpen) {
            closeDrawer()
        }
        BoxWithConstraints(
            searchModeModifier
                .fillMaxSize()
                .focusable(enabled = !isSearching),
            contentAlignment = Alignment.Center
        ) {
            Scaffold(Modifier.fillMaxSize(), topBar = {
                TopAppBar(
                    title = {
                        Text(text = currentUser.name ?: currentUser.email)
                    }, navigationIcon = {
                        IconButton(onClick = {
                            openDrawer()
                        }) {
                            Icon(Icons.Rounded.Menu, "open menu")
                        }
                    })
            }, floatingActionButton = {
                FloatingActionButton(onClick = {
                    setIsSearching(true)
                }) {
                    Icon(Icons.Rounded.Search, "search users")
                }
            }, floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = chatState
                ) {
                    items(chats) { chat ->
                        ChatListItem(
                            currentUser,
                            chat,
                            maxWidth
                        ) {
                            openChat(it.id)
                        }
                    }
                }
            }
        }
        if (signOutVisible) {
            setIsSearching(false)
            SignOutDialog(confirm = {
                signOut()
            }, dismiss = {
                setSignOutVisible(false)
            })
        }
    }
    if (isSearching) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            SearchScreen(scaffoldState, currentUser.id, onItemClick = {
                createChat(it)
            }) {
                setIsSearching(false)
            }
        }
    }
}