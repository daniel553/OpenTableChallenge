package com.opentable.domain.usecase

import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.domain.model.Reservation
import com.opentable.domain.model.toReservationEntity
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SaveReservation @Inject constructor(
    private val repository: IReservationRepository
) {
    suspend operator fun invoke(reservation: Reservation): Long {
        return repository.insertToDb(reservation.toReservationEntity())
    }
}