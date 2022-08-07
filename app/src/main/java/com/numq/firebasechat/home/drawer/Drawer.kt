package com.numq.firebasechat.home.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    currentUser: User,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDrawerArticleClick: (DrawerArticle) -> Unit,
    onDrawerOpened: () -> Unit = {},
    onDrawerClosed: () -> Unit = {},
    content: @Composable (() -> Unit, () -> Unit) -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val openDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.open()
        }
    }
    val closeDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    BoxWithConstraints {
        ModalDrawer(drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen, drawerContent = {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Image(
                                Icons.Rounded.Person, "", modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape), contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(8.dp)) {
                                if (currentUser.name != null) {
                                    Text(text = currentUser.name, fontWeight = FontWeight.Medium)
                                    Text(text = currentUser.email, fontWeight = FontWeight.Light)
                                } else {

                                    Text(text = currentUser.email, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        DrawerItem(DrawerArticle.Settings, onDrawerArticleClick)
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Divider()
                            DrawerItem(DrawerArticle.SignOut, onDrawerArticleClick)
                        }
                    }
                }
            }) {
            content(openDrawer, closeDrawer)
//            BuildChats(currentUser, activeChat, chats, maxWidth,
//                onItemClick = {
//                    vm.updateLastActiveChatId(currentUser.copy(lastActiveChatId = it.id))
//                    chatIsOpen = true
//                },
//                onDrawerOpen = {
//                    openDrawer()
//                    chatIsOpen = false
//                },
//                onSearch = { activeDialog = DialogItem.SEARCH })
//        }
//        activeChat?.let { chat ->
//            BuildActiveChat(currentUser, chat, chatIsOpen, maxWidth, vm) {
//                chatIsOpen = it
//            }
//        }
        }
    }
}