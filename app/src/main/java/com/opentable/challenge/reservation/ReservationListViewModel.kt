package com.opentable.challenge.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentable.challenge.model.toListReservationItem
import com.opentable.domain.model.Reservation
import com.opentable.domain.usecase.GetAllReservations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 💡The reservation list view model will use main flows, one for the up going events and other for
 * the state changes.
 */
@HiltViewModel
class ReservationListViewModel @Inject constructor(
    //💡Space to inject the use cases.
    private val getAllReservations: GetAllReservations
) : ViewModel() {

    private val _uiState: MutableStateFlow<ReservationListState> =
        MutableStateFlow(ReservationListState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReservationListEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllReservations()
                .onEach { reservations ->
                    _uiState.update {
                        ReservationListState.Success(reservations.toListReservationItem())
                    }
                }
                .catch {
                    _uiState.update {
                        ReservationListState.Error
                    }
                }
                .stateIn(viewModelScope)
        }
    }

    fun onEvent(event: ReservationListEvent) {
        TODO("Handle event")
    }

}