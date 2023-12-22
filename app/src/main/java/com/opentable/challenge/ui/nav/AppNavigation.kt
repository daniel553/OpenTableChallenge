package com.opentable.challenge.ui.nav

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// 💡Main navigation will hold the list of reservation and add list views
@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Router.ReservationScreen.destination) {
        composable(
            route = Router.ReservationScreen.destination,
        ) {
            Text("Reservation list")
        }
    }
}