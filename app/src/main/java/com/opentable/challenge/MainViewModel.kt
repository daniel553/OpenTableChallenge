package com.opentable.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentable.challenge.ui.layout.AppScaffoldState
import com.opentable.challenge.ui.layout.ScaffoldEvent
import com.opentable.challenge.ui.nav.MainNavigationEvent
import com.opentable.challenge.ui.nav.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MainUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onScaffoldEvent(event: ScaffoldEvent) {
        viewModelScope.launch {
            when (event) {
                ScaffoldEvent.AddPressed -> _uiEvent.emit(MainUIEvent.OnNavPush(Router.AddReservationScreen.destination))
                ScaffoldEvent.BackPressed -> _uiEvent.emit(MainUIEvent.OnNavPop)
                is ScaffoldEvent.OnDestinationChanged -> updateDestination(
                    event.hasBackStack,
                    event.destination
                )
                ScaffoldEvent.OnSnackBarShown -> showSnackBar(null)
            }
        }
    }

    fun onMainNavigationEvent(event: MainNavigationEvent) {
        viewModelScope.launch {
            when (event) {
                is MainNavigationEvent.OnError -> _uiEvent.emit(MainUIEvent.OnShowSnackBar(event.message))
            }
        }
    }


    private fun updateDestination(hasBackStack: Boolean, destination: String) {
        _uiState.update { state ->
            state.copy(
                scaffoldState = state.scaffoldState.copy(
                    showBack = hasBackStack,
                    title = destination
                )
            )
        }
    }

    fun showSnackBar(message: String?) {
        _uiState.update { state -> state.copy(scaffoldState = state.scaffoldState.copy(snackBar = message)) }
    }

}

sealed interface MainUIEvent {
    data class OnNavPush(val destination: String) : MainUIEvent
    data object OnNavPop : MainUIEvent
    data class OnShowSnackBar(val message: String?) : MainUIEvent
}

data class MainUIState(val scaffoldState: AppScaffoldState = AppScaffoldState())