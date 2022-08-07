package com.numq.firebasechat.home.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerArticle(
    val icon: ImageVector,
    val title: String,
    val route: String? = null
) {

    private companion object {
        const val SETTINGS_ROUTE = "settings"
        const val SIGN_OUT_ROUTE = "sign_out"
    }

    object Settings : DrawerArticle(
        Icons.Rounded.Settings,
        "Settings",
        SETTINGS_ROUTE
    )

    object SignOut : DrawerArticle(
        Icons.Rounded.ExitToApp,
        "SignOut",
        SIGN_OUT_ROUTE
    )

}