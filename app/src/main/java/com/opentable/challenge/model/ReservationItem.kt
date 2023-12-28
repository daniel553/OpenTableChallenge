package com.opentable.challenge.model

import com.opentable.challenge.util.toMilliseconds
import com.opentable.challenge.util.toTimeString
import com.opentable.domain.model.Reservation

/**
 * 💡This is a scoped model used in views like "Compose reservation object"
 */
data class ReservationItem(
    val id: Long = 0L,
    val name: String = "",
    val time: Long = 0L, // Used to sort it
    val timeString: String = "0:00 AM" // time formatted
)

// 💡 Used in previews
internal fun reservationItemPreviewMock(times: Int): List<ReservationItem> =
    (0 until times).map { ReservationItem(it.toLong(), "Name $it", it.toLong()) }

// Transformations -----------

fun List<Reservation>.toListReservationItem(): List<ReservationItem> = this.map {
    it.toReservationItem()
}

fun Reservation.toReservationItem(): ReservationItem = ReservationItem(
    id = this.id,
    name = this.name,
    time = this.time.toMilliseconds(),
    timeString = this.time.toTimeString()
)