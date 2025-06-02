package com.holparb.echojournal.app.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object EchoList: NavigationRoute

    @Serializable
    data class CreateEcho(
        val recordingPath: String,
        val duration: Long,
        val amplitudes: String
    ): NavigationRoute
}