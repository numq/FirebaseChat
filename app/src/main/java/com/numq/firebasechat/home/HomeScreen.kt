package com.numq.firebasechat.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.firebase.auth.FirebaseAuthException
import com.numq.firebasechat.chat.ActiveChatScreen
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.chat.ChatListItem
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.home.dialog.SignOutDialog
import com.numq.firebasechat.home.drawer.Drawer
import com.numq.firebasechat.home.drawer.DrawerArticle
import com.numq.firebasechat.search.SearchScreen
import com.numq.firebasechat.user.User

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    navigateToAuth: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("HOME", state.toString())
    }

    state.exception?.let {
        when (it) {
            is FirebaseAuthException -> LaunchedEffect(Unit) {
                vm.signOut(navigateToAuth)
            }
            else -> ShowError(scaffoldState, it, vm.cleanUpError)
        }
    }

    state.currentUser?.let { user ->
        BuildHome(
            scaffoldState,
            currentUser = user,
            chats = state.chats,
            activeChat = state.activeChat,
            createChat = {
                vm.createChat(user.id, it.id)
            },
            updateActiveChat = { chat ->
                if (chat.id != user.lastActiveChatId) vm.updateLastActiveChat(user.id, chat.id)
            },
            signOut = {
                vm.signOut(navigateToAuth)
            })
    }

}

@Composable
fun BuildHome(
    scaffoldState: ScaffoldState,
    currentUser: User,
    chats: List<Chat>,
    activeChat: Chat?,
    createChat: (User) -> Unit,
    updateActiveChat: (Chat) -> Unit,
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

    Drawer(currentUser, onDrawerArticleClick = {
        when (it) {
            is DrawerArticle.Settings -> Unit
            is DrawerArticle.SignOut -> setSignOutVisible(true)
        }
    }) { openDrawer, closeDrawer ->
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
                IconButton(onClick = {
                    setIsSearching(true)
                }) {
                    Icon(Icons.Rounded.Search, "search users")
                }
            }, floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(chats) { chat ->
                        ChatListItem(
                            currentUser,
                            chat,
                            maxWidth - 50.dp,
                            updateActiveChat
                        )
                    }
                }
                activeChat?.let {
                    closeDrawer()
                }
            }
            activeChat?.let { chat ->
                ActiveChatScreen(
                    currentUser,
                    chat,
                    maxWidth
                )
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
                SearchScreen(scaffoldState, onItemClick = {
                    createChat(it)
                }) {
                    setIsSearching(false)
                }
            }
        }
    }
}