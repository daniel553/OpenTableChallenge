package com.opentable.challenge.reservation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 💡The reservation list view model will use main flows, one for the up going events and other for
 * the state changes.
 */
@HiltViewModel
class ReservationListViewModel @Inject constructor(
    //💡Space to inject the use cases.
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationListState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReservationListEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: ReservationListEvent) {
        TODO("Handle event")
    }
}