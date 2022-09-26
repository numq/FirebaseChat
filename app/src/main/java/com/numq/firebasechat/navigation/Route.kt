package com.numq.firebasechat.navigation

sealed class Route private constructor(
    val name: String,
    val destination: String = name.lowercase()
) {

    private companion object {
        const val SPLASH = "splash"
        const val AUTH = "auth"
        const val CHAT = "chat"
        const val HOME = "home"
        const val SETTINGS = "settings"
    }

    object Splash : Route(SPLASH)
    object Auth : Route(AUTH)
    object Chat : Route(CHAT)
    object Home : Route(HOME)
    object Settings : Route(SETTINGS)
}