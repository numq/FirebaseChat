package com.numq.firebasechat.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.numq.firebasechat.auth.AuthScreen
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.home.HomeScreen
import com.numq.firebasechat.network.NetworkStatus
import com.numq.firebasechat.network.NetworkStatusNotification
import com.numq.firebasechat.permission.PermissionsRequired
import com.numq.firebasechat.settings.SettingsScreen
import com.numq.firebasechat.splash.SplashScreen
import kotlinx.coroutines.withTimeout

@Composable
fun Router(vm: NavViewModel = hiltViewModel()) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val permissions = listOf<String>(
//        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    LaunchedEffect(navBackStackEntry) {
        Log.e("NAV", navBackStackEntry?.destination.toString())
    }

    PermissionsRequired(permissions) {

        val state by vm.state.collectAsState()

        state.exception?.let {
            ShowError(scaffoldState, it, vm.cleanUpError)
        }

        LaunchedEffect(state.userId) {
            state.userId?.let {
                navController.navigate(Route.Home.destination + "/${state.userId}") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            } ?: run {
                if (state.status is NetworkStatus.Available) {
                    withTimeout(1500) {
                        navController.navigate(Route.Auth.destination) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }

        Scaffold(scaffoldState = scaffoldState) { paddingValues ->
            Box(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                NavHost(
                    navController,
                    startDestination = Route.Splash.destination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Route.Splash.destination) {
                        SplashScreen()
                    }
                    composable(Route.Auth.destination) {
                        AuthScreen(scaffoldState)
                    }
                    composable(Route.Home.destination + "/{userId}") {
                        it.arguments?.getString("userId")?.let { userId ->
                            HomeScreen(
                                scaffoldState,
                                userId = userId,
                                navigateToSettings = {
                                    navController.navigate(Route.Settings.destination + "/$userId") {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                    composable(Route.Settings.destination + "/{userId}") {
                        SettingsScreen(
                            scaffoldState,
                            userId = it.arguments?.getString("userId")
                        ) {
                            navController.navigateUp()
                        }
                    }
                }
                state.status?.let { status ->
                    NetworkStatusNotification(status)
                }
            }
        }
    }
}