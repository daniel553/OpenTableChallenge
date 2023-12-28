package com.opentable.data.reservation.repo

import com.opentable.data.reservation.repo.db.ReservationDao
import com.opentable.data.reservation.repo.db.ReservationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface IReservationLocalDatasource {
    val reservations: Flow<List<ReservationEntity>>

    suspend fun insert(reservation: ReservationEntity): Long
    suspend fun getById(id: Long): ReservationEntity
    suspend fun delete(reservation: ReservationEntity)
    suspend fun getByDate(date: String): List<ReservationEntity>
}

/**
 * 💡Use a datasource as a source of truth on local data.
 */
@Singleton
class ReservationLocalDatasource @Inject constructor(
    private val dao: ReservationDao
) : IReservationLocalDatasource {

    /**
     * 💡Smooth integration of Flow with DAOs of Room
     */
    override val reservations: Flow<List<ReservationEntity>>
        get() = dao.all()

    override suspend fun insert(reservation: ReservationEntity): Long = dao.insert(reservation)

    override suspend fun getById(id: Long): ReservationEntity = dao.getById(id)

    override suspend fun delete(reservation: ReservationEntity) = dao.delete(reservation)
    override suspend fun getByDate(date: String): List<ReservationEntity> = dao.getByDate("%$date%")
}

