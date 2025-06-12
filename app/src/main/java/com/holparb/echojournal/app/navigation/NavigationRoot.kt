package com.holparb.echojournal.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.holparb.echojournal.echoes.presentation.create_echo.CreateEchoRoot
import com.holparb.echojournal.echoes.presentation.echo_list.EchoListRoot
import com.holparb.echojournal.echoes.presentation.settings.SettingsRoot
import com.holparb.echojournal.echoes.presentation.util.toCreateEchoRoute

const val ACTION_CREATE_ECHO = "com.holparb.CREATE_ECHO"

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.EchoList(
            startRecording = false
        )
    ) {
        composable<NavigationRoute.EchoList>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.EchoList>(
                    basePath = "https:/echojournal.com/echoes"
                ) {
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
            EchoListRoot(
                onNavigateToCreateEcho = { recordingDetails ->
                    navController.navigate(recordingDetails.toCreateEchoRoute())
                },
                onNavigateToSettings = {
                    navController.navigate(NavigationRoute.Settings)
                }
            )
        }
        composable<NavigationRoute.CreateEcho> {
            CreateEchoRoot(
                onNavigateBack = navController::navigateUp
            )
        }
        composable<NavigationRoute.Settings> {
            SettingsRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}