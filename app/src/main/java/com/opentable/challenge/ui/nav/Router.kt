package com.opentable.challenge.ui.nav

/**
 * 💡Define the router with a set of screens  using a simple sealed interface.
 * basically the destination are simple strings used by nav controller
 */
sealed class Router(val destination: String) {

    data object ReservationScreen: Router("ReservationScreen")
    data object AddReservationScreen: Router("AddReservationScreen")
}