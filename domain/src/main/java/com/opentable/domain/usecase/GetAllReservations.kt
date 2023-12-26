package com.opentable.domain.usecase

import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.domain.model.Reservation
import com.opentable.domain.model.toFlowReservationList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 💡Subscribes and maps to domain model (or business logic model) the reservations
 */
class GetAllReservations @Inject constructor(
    private val reservationRepository: IReservationRepository
) {

    //💡a single operation invoke can help to ensure 1 single action on use case
    operator fun invoke(): Flow<List<Reservation>> =
        reservationRepository.reservations.toFlowReservationList()
}