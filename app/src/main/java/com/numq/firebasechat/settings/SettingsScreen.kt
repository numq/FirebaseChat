package com.numq.firebasechat.settings

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.numq.firebasechat.auth.InputValidator
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.user.User

@Composable
fun SettingsScreen(
    scaffoldState: ScaffoldState,
    userId: String?,
    vm: SettingsViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {

    BackHandler {
        navigateUp()
    }

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("SETTINGS", state.toString())
        userId?.let(vm::getUserById) ?: navigateUp()
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
        }, onExitSettings = navigateUp)
    } ?: navigateUp()

}

@Composable
fun BuildSettings(
    user: User,
    onUploadImage: (ByteArray) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onExitSettings: () -> Unit
) {

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                onUploadImage(inputStream.readBytes())
            }
        }
    }

    val (name, setName) = rememberSaveable {
        mutableStateOf("")
    }

    val (email, setEmail) = rememberSaveable {
        mutableStateOf("")
    }

    val (password, setPassword) = rememberSaveable {
        mutableStateOf("")
    }

    val (passwordVisible, setPasswordVisible) = rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Settings")
        }, navigationIcon = {
            IconButton(onClick = {
                onExitSettings()
            }) {
                Icon(Icons.Rounded.ArrowBack, "exit settings")
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = user.imageUri,
                    contentDescription = "user profile image",
                    modifier = Modifier
                        .size(maxWidth.times(.8f))
                        .clip(CircleShape)
                        .clickable {
                            imagePicker.launch("image/*")
                        },
                    contentScale = ContentScale.Crop,
                    placeholder = rememberVectorPainter(Icons.Rounded.Person),
                    fallback = rememberVectorPainter(Icons.Rounded.Person)
                )
            }
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(value = name, onValueChange = setName,
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Rounded.Person, "person icon") },
                        trailingIcon = {
                            if (name.isNotBlank()) IconButton(onClick = { setName("") }) {
                                Icon(Icons.Rounded.Clear, "clear name")
                            }
                        }, singleLine = true, placeholder = {
                            Text(text = user.name)
                        }, isError = InputValidator.validateName(name)
                    )
                    AnimatedVisibility(InputValidator.validateName(name)) {
                        IconButton(onClick = {
                            onNameChange(name)
                            setName("")
                        }, enabled = name.isNotBlank()) {
                            Icon(Icons.Rounded.Done, "confirm name change")
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(value = email, onValueChange = setEmail,
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Rounded.Person, "person icon") },
                        trailingIcon = {
                            if (email.isNotBlank()) IconButton(onClick = { setEmail("") }) {
                                Icon(Icons.Rounded.Clear, "clear email")
                            }
                        }, singleLine = true, placeholder = {
                            Text(text = user.email)
                        }, isError = InputValidator.validateEmail(email)
                    )
                    AnimatedVisibility(InputValidator.validateEmail(email)) {
                        IconButton(onClick = {
                            onEmailChange(email)
                            setEmail("")
                        }, enabled = email.isNotBlank()) {
                            Icon(Icons.Rounded.Done, "confirm email change")
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(value = password, onValueChange = setPassword,
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            if (password.isNotBlank()) {
                                IconButton(onClick = {
                                    setPasswordVisible(!passwordVisible)
                                }) {
                                    if (passwordVisible) Icon(
                                        Icons.Rounded.Visibility,
                                        "visible password icon"
                                    )
                                    else Icon(Icons.Rounded.VisibilityOff, "hidden password icon")
                                }
                            } else Icon(Icons.Rounded.Password, "password icon")
                        },
                        trailingIcon = {
                            if (password.isNotBlank()) IconButton(onClick = { setPassword("") }) {
                                Icon(Icons.Rounded.Clear, "clear password")
                            }
                        }, singleLine = true, placeholder = {
                            Text(text = "Type new password here")
                        }, isError = InputValidator.validatePassword(name),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    AnimatedVisibility(InputValidator.validatePassword(password)) {
                        IconButton(onClick = {
                            onPasswordChange(password)
                            setPassword("")
                        }, enabled = password.isNotBlank()) {
                            Icon(Icons.Rounded.Done, "confirm password change")
                        }
                    }
                }
            }
        }
    }

}