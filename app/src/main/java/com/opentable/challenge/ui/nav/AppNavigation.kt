package com.opentable.challenge.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.opentable.challenge.reservation.ReservationListScreen
import com.opentable.challenge.reservation.ReservationListViewModel

// 💡Main navigation will hold the list of reservation and add list views
@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Router.ReservationScreen.destination
    ) {
        composable(
            route = Router.ReservationScreen.destination,
        ) {
            val viewmodel: ReservationListViewModel = hiltViewModel()
            // 💡Be aware of the lifecycle by collecting as state using this extension fun
            val state by viewmodel.uiState.collectAsStateWithLifecycle()
            ReservationListScreen(state = state, onEvent = viewmodel::onEvent)
        }
    }
}