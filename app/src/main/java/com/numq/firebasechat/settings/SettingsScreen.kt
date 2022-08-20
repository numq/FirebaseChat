package com.numq.firebasechat.settings

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.user.User

@Composable
fun SettingsScreen(
    scaffoldState: ScaffoldState,
    navBackStackEntry: NavBackStackEntry,
    vm: SettingsViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {

    BackHandler {
        navigateUp()
    }

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("SETTINGS", state.toString())
        navBackStackEntry.arguments?.getString("id")?.let(vm::updateUser)
    }

    state.exception?.let {
        ShowError(scaffoldState, it, vm.cleanUpError)
    }

    state.currentUser?.let { user ->
        BuildSettings(user, onUploadImage = {
            vm.uploadImage(user.id, it)
        }, onNameChange = {
            vm.updateName(user.id, it)
        }, onEmailChange = {
            vm.updateEmail(user.id, it)
        }, onPasswordChange = {
            vm.changePassword(user.id, it)
        })
    } ?: Text("SETTINGS")

}

@Composable
fun BuildSettings(
    user: User,
    onUploadImage: (ByteArray) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
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

    val (name, setName) = remember {
        mutableStateOf(user.name ?: "")
    }

    val (email, setEmail) = remember {
        mutableStateOf(user.email)
    }

    val (password, setPassword) = remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            AsyncImage(
                model = user.imageUri,
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
        }
        Spacer(modifier = Modifier.height(16.dp))
        user.name?.let {
            Text(text = "My name is ${user.name}, but I want to be known as $name")
        } ?: Text(text = "Now my name will be $name")
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = name, onValueChange = setName, trailingIcon = {
                IconButton(onClick = { setName("") }) {
                    Icon(Icons.Rounded.Clear, "clear name")
                }
            }, singleLine = true)
            IconButton(onClick = {
                onNameChange(name)
                setName("")
            }) {
                Icon(Icons.Rounded.Done, "confirm name change")
            }
        }
        Divider()
        Text(text = "Change my email from ${user.email} to $email")
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = email, onValueChange = setEmail, trailingIcon = {
                IconButton(onClick = { setEmail("") }) {
                    Icon(Icons.Rounded.Clear, "clear email")
                }
            }, singleLine = true)
            IconButton(onClick = {
                onEmailChange(email)
                setEmail("")
            }) {
                Icon(Icons.Rounded.Done, "confirm email change")
            }
        }
        Divider()
        Text(text = "Update my password")
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = password, onValueChange = setPassword, trailingIcon = {
                IconButton(onClick = { setPassword("") }) {
                    Icon(Icons.Rounded.Clear, "clear password")
                }
            }, singleLine = true)
            IconButton(onClick = {
                onPasswordChange(password)
                setPassword("")
            }) {
                Icon(Icons.Rounded.Done, "confirm password change")
            }
        }
    }
}