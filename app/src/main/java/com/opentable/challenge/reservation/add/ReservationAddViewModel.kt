package com.opentable.challenge.reservation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentable.challenge.model.toListReservationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 💡The intention of this view model is to handle events on reservations, for now just add event
 * (probably can be reused for edit).
 */
@HiltViewModel
class ReservationAddViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableStateFlow<ReservationAddFormState> =
        MutableStateFlow(ReservationAddFormState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReservationAddEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun onEvent(event: ReservationAddEvent) {
        TODO("Handle event")
    }

}