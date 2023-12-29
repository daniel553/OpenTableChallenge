package com.opentable.challenge.ui.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opentable.challenge.reservation.ReservationListEvent
import com.opentable.challenge.reservation.ReservationListScreen
import com.opentable.challenge.reservation.ReservationListViewModel
import com.opentable.challenge.reservation.add.ReservationAddEvent
import com.opentable.challenge.reservation.add.ReservationAddScreen
import com.opentable.challenge.reservation.add.ReservationAddViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 💡Main navigation will hold the list of reservation and add list views
@Composable
fun MainNavigation(navController: NavHostController, onEvent: (MainNavigationEvent) -> Unit) {
    NavHost(
        navController = navController,
        startDestination = Router.ReservationScreen.destination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Router.ReservationScreen.destination,
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }
        ) {
            val viewmodel: ReservationListViewModel = hiltViewModel()
            // 💡Be aware of the lifecycle by collecting as state using this extension fun
            val state by viewmodel.uiState.collectAsStateWithLifecycle()

            // 💡Launch effect on view model change, but
            LaunchedEffect(viewmodel) {
                viewmodel.uiEvent.onEach { event ->
                    when (event) {
                        ReservationListEvent.onAdd -> launch {
                            navController.navigate(Router.AddReservationScreen.destination)
                        }

                        is ReservationListEvent.onSelected -> {
                            /* TODO: No action yet*/
                        }
                    }
                }.stateIn(this)
            }
            ReservationListScreen(state = state, onEvent = viewmodel::onEvent)
        }

        composable(
            route = Router.AddReservationScreen.destination,
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                    animationSpec = tween(300),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }
        ) {
            val viewmodel: ReservationAddViewModel = hiltViewModel()
            val state by viewmodel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(viewmodel) {
                viewmodel.uiEvent.onEach { event ->
                    when (event) {
                        is ReservationAddEvent.OnError -> launch {
                            onEvent(
                                MainNavigationEvent.OnError(event.message)
                            )
                        }

                        ReservationAddEvent.OnBack,
                        ReservationAddEvent.OnReservationSaved -> launch {
                            navController.popBackStack()
                        }

                        else -> {}
                    }
                }.stateIn(this)
            }
            ReservationAddScreen(state = state, onEvent = viewmodel::onEvent)
        }
    }
}

sealed interface MainNavigationEvent {
    data class OnError(val message: String?) : MainNavigationEvent
}