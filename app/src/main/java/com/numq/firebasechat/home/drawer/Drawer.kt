package com.numq.firebasechat.home.drawer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.numq.firebasechat.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    currentUser: User,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onUploadImage: (ByteArray) -> Unit,
    onDrawerArticleClick: (DrawerArticle) -> Unit,
    onDrawerOpened: () -> Unit = {},
    onDrawerClosed: () -> Unit = {},
    content: @Composable (() -> Unit, () -> Unit) -> Unit
) {

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.openInputStream(uri)?.readBytes()?.let(onUploadImage)
            }
        }
    )

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
                            AsyncImage(
                                model = currentUser.imageUri,
                                contentDescription = "user profile image",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        imagePicker.launch("image/*")
                                    },
                                contentScale = ContentScale.Crop,
                                placeholder = rememberVectorPainter(Icons.Rounded.Person),
                                fallback = rememberVectorPainter(Icons.Rounded.Person)
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
        }
    }
}