package com.opentable.domain.model

import com.opentable.data.reservation.repo.db.ReservationEntity
import com.opentable.domain.util.toLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

/**
 * 💡Business logic model
 */
data class Reservation(
    val id: Long = 0L,
    val name: String = "",
    val time: LocalDateTime
)

// Transformations ---------
fun Flow<List<ReservationEntity>>.toFlowReservationList(): Flow<List<Reservation>> =
    this.map { reservationList -> reservationList.map { it.toReservation() } }

fun ReservationEntity.toReservation(): Reservation = Reservation(
    id = this.id,
    name = this.name,
    time = this.time.toLocalDateTime()
)

fun Reservation.toReservationEntity(): ReservationEntity = ReservationEntity(
    name = this.name,
    time = this.time.toString()
)

fun List<ReservationEntity>.toReservationList(): List<Reservation> = this.map {
    Reservation(id = it.id, name = it.name, time = it.time.toLocalDateTime())
}