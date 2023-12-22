package com.opentable.challenge.reservation

import com.opentable.challenge.model.ReservationItem

/**
 * 💡This auxiliary file is used to define the state and the events associated to view,
 * the intention is to seal to modifications and visualize all cases covered
 */
sealed interface ReservationListState {
    data object Loading : ReservationListState
    data object Error : ReservationListState
    data class Success(val list: List<ReservationItem>) : ReservationListState
}

sealed interface ReservationListEvent {
    data class onSelected(val reservation: ReservationItem) : ReservationListEvent
    // and more
}