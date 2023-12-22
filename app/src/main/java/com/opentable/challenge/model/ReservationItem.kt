package com.opentable.challenge.model

/**
 * 💡This is a scoped model used in views like "Compose reservation object"
 */
data class ReservationItem(
    val id: Long,
    val name: String,
    val time: Long, // Used to sort it
    val timeString: String = "0:00 AM" // time formatted
)

// 💡 Used in previews
internal fun reservationItemPreviewMock(times: Int): List<ReservationItem> =
    (0 until times).map { ReservationItem(it.toLong(), "Name $it", it.toLong()) }