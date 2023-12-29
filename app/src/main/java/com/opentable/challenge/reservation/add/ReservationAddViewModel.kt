package com.opentable.challenge.reservation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.model.toReservation
import com.opentable.challenge.ui.component.DropdownMenuItem
import com.opentable.domain.usecase.GetAvailability
import com.opentable.domain.usecase.SaveReservation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 💡The intention of this view model is to handle events on reservations, for now just add event
 * (probably can be reused for edit).
 */
@HiltViewModel
class ReservationAddViewModel @Inject constructor(
    private val getAvailability: GetAvailability,
    private val saveReservation: SaveReservation
) : ViewModel() {

    private val _uiState: MutableStateFlow<ReservationAddFormState> =
        MutableStateFlow(ReservationAddFormState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReservationAddEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var availability = mutableListOf<Triple<LocalDateTime, String, Boolean>>()

    init {
        initAvailability()
    }

    fun onEvent(event: ReservationAddEvent) {
        when (event) {
            ReservationAddEvent.OnSave -> saveForm()
            is ReservationAddEvent.OnUpdate -> updateForm(event.reservation)
            is ReservationAddEvent.OnBack -> goBack()
            else -> {}
        }
    }

    private fun initAvailability() {
        viewModelScope.launch {
            showLoading(true)
            availability.clear()
            availability.addAll(getAvailability())
            _uiState.update { state ->
                state.copy(
                    timeOptions = availability.map { (key, text, enabled) ->
                        DropdownMenuItem(
                            key.toString(),
                            text,
                            !enabled
                        )
                    },
                    noMoreTimes = noAvailability()
                )
            }
            showLoading(false)
        }
    }

    // 💡Shows progress indicator
    private fun showLoading(loading: Boolean) {
        _uiState.update { state -> state.copy(loading = loading) }
    }

    // 💡Check errors in form and enable saving button
    private fun updateForm(reservationItem: ReservationItem) {
        _uiState.update { state ->
            state.copy(
                reservation = reservationItem,
                errors = formErrors(reservationItem),
                errorSave = false
            )
        }
    }

    private fun formErrors(reservationItem: ReservationItem): Set<ReservationAddFormError> {
        val set = mutableSetOf<ReservationAddFormError>()

        if (reservationItem.name.isEmpty()) {
            set.add(ReservationAddFormError.NameRequired)
        }
        if (reservationItem.time == 0L) {
            set.add(ReservationAddFormError.TimeRequired)
        }

        return set.toSet()
    }

    private fun saveForm() {
        showLoading(true)
        setSaveError(false)
        with(uiState.value) {
            if (errors.isEmpty()) {
                viewModelScope.launch {
                    try {
                        val reservationToSave = reservation.copy(
                            timeString = getAvailabilityTimeString(reservation.timeString)
                        )
                        val result = saveReservation(reservationToSave.toReservation())
                        if (result > 0) {
                            //Saved correctly
                            showError("Saved!") // 💡Don't do this, it hast to be a reference of localized string
                            _uiEvent.emit(ReservationAddEvent.OnReservationSaved)
                        } else {
                            //Error saving
                            showError("Error saving reservation")
                            setSaveError(true)
                        }
                    } catch (exception: IOException) {
                        showError(exception.message)
                        setSaveError(true)
                    } finally {
                        showLoading(false)
                    }
                }
            } else {
                showError("Form has errors")
                showLoading(false)
                setSaveError(true)
            }
        }
    }

    private fun goBack() {
        viewModelScope.launch {
            _uiEvent.emit(ReservationAddEvent.OnBack)
        }
    }

    private fun setSaveError(errorSave: Boolean) {
        _uiState.update { state -> state.copy(errorSave = errorSave) }
    }

    private fun getAvailabilityTimeString(timeString: String): String {
        return availability.find { (_, str) -> str == timeString }?.first?.toString()
            ?: throw IOException("Unable to set reservation time from availabilities")
    }

    private fun showError(message: String? = "") {
        viewModelScope.launch {
            _uiEvent.emit(ReservationAddEvent.OnError(message))
        }
    }

    // 💡warns about availability for a reservation as a Chip
    private fun noAvailability(): Boolean =
        availability.isEmpty() || availability.find { !it.third } == null

}