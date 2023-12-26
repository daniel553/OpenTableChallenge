package com.opentable.domain.model

import com.opentable.data.reservation.repo.db.ReservationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 💡Business logic model
 */
data class Reservation(
    val id: Long = 0L,
    val name: String = "",
    val time: Long = 0L
)

// Transformations ---------
fun Flow<List<ReservationEntity>>.toFlowReservationList(): Flow<List<Reservation>> =
    this.map { reservationList -> reservationList.map { it.toReservation() } }

fun ReservationEntity.toReservation(): Reservation = Reservation(
    id = this.id,
    name = this.name,
    time = this.time
)
