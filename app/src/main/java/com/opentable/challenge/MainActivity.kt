package com.opentable.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.opentable.challenge.ui.layout.AppScaffold
import com.opentable.challenge.ui.layout.ScaffoldEvent
import com.opentable.challenge.ui.nav.MainNavigation
import com.opentable.challenge.ui.theme.OpenTableChallengeTheme
import com.opentable.challenge.util.navigateDisctinct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/**
 * 💡Activity as hilt entry point, the intention is to have a single activity with navigation and
 * screens.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenTableChallengeTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle(this)

                //💡Used to register a destination change listener and events on ui
                LaunchedEffect(viewModel) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        viewModel.onScaffoldEvent(
                            ScaffoldEvent.OnDestinationChanged(
                                hasBackStack = navController.previousBackStackEntry != null,
                                destination = destination.route.orEmpty()
                            )
                        )
                    }
                    viewModel.uiEvent.onEach { event ->
                        when (event) {
                            MainUIEvent.OnNavPop -> navController.popBackStack()
                            is MainUIEvent.OnNavPush -> navController.navigateDisctinct(event.destination)
                            is MainUIEvent.OnShowSnackBar -> viewModel.showSnackBar(event.message)
                        }
                    }.stateIn(this)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(
                        state = state.scaffoldState,
                        scaffoldEvent = viewModel::onScaffoldEvent
                    ) {
                        MainNavigation(navController, viewModel::onMainNavigationEvent)
                    }
                }
            }
        }
    }
}