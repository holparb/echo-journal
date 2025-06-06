package com.holparb.echojournal.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.holparb.echojournal.echoes.presentation.create_echo.CreateEchoRoot
import com.holparb.echojournal.echoes.presentation.echo_list.EchoListRoot
import com.holparb.echojournal.echoes.presentation.util.toCreateEchoRoute

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.EchoList
    ) {
        composable<NavigationRoute.EchoList> {
            EchoListRoot(
                onNavigateToCreateEcho = { recordingDetails ->
                    navController.navigate(recordingDetails.toCreateEchoRoute())
                }
            )
        }
        composable<NavigationRoute.CreateEcho> {
            CreateEchoRoot(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}