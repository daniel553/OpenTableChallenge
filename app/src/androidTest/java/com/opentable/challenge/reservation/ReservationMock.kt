package com.opentable.challenge.reservation

import com.opentable.challenge.model.ReservationItem

internal fun reservationItems(count: Int): List<ReservationItem> = (0 until count).map {
    ReservationItem(it.toLong(), "name$it", it.toLong(), timeString = "1:00 PM")
}