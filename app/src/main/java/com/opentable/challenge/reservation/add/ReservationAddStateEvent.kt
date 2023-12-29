package com.opentable.challenge.reservation.add

import com.opentable.challenge.model.ReservationItem
import com.opentable.challenge.ui.component.DropdownMenuItem

//💡Form may need extra attributes, the reservation could hold some data (at least required)
data class ReservationAddFormState(
    val reservation: ReservationItem = ReservationItem(),
    val loading: Boolean = false,
    val errors: Set<ReservationAddFormError> = emptySet(),
    val timeOptions: List<DropdownMenuItem> = emptyList(),
    val errorSave: Boolean = false,
    val noMoreTimes: Boolean = false,
)

enum class ReservationAddFormError {
    NameRequired,
    TimeRequired,
    SaveError
}

//💡if needed, add event could be add or edit
sealed interface ReservationAddEvent {
    data class OnUpdate(val reservation: ReservationItem) : ReservationAddEvent
    data object OnSave : ReservationAddEvent
    data object OnReservationSaved : ReservationAddEvent
    data class OnError(val message: String?) : ReservationAddEvent
    data object OnBack : ReservationAddEvent
}