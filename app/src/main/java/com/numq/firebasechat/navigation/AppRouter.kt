package com.numq.firebasechat.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.numq.firebasechat.auth.AuthScreen
import com.numq.firebasechat.home.HomeScreen
import com.numq.firebasechat.permission.PermissionsRequired
import com.numq.firebasechat.splash.SplashScreen

@Composable
fun AppRouter() {

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
        Scaffold(scaffoldState = scaffoldState) { paddingValues ->
            Box(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController,
                    startDestination = Route.Splash.destination
                ) {
                    composable(Route.Splash.destination) {
                        SplashScreen(scaffoldState, navigateToAuth = {
                            navController.navigate(Route.Auth.destination) {
                                popUpTo(Route.Splash.destination) {
                                    inclusive = true
                                }
                            }
                        }, navigateToHome = {
                            navController.navigate(Route.Home.destination) {
                                popUpTo(Route.Splash.destination) {
                                    inclusive = true
                                }
                            }
                        })
                    }
                    composable(Route.Auth.destination) {
                        AuthScreen(scaffoldState, navigateToHome = {
                            navController.navigate(Route.Home.destination) {
                                popUpTo(Route.Auth.destination) {
                                    inclusive = true
                                }
                            }
                        })
                    }
                    composable(Route.Home.destination) {
                        HomeScreen(
                            scaffoldState,
                            navigateToAuth = {
                                navController.navigate(Route.Auth.destination) {
                                    popUpTo(Route.Home.destination) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}