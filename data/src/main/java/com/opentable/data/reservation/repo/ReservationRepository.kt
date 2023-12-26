package com.opentable.data.reservation.repo

import com.opentable.data.reservation.repo.db.ReservationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 💡Personally I like "I" as a prefix for interfaces, but commonly devs use "Impl" to differentiate
 * the interface vs its implementation
 */
interface IReservationRepository {
    val reservations: Flow<List<ReservationEntity>>

    suspend fun insertToDb(reservation: ReservationEntity): Long
    suspend fun getById(id: Long): ReservationEntity
    suspend fun deleteFromDb(reservation: ReservationEntity)
    //💡Then register other actions like Fetch (from api) then Save (to db) a new entity
}

@Singleton
class ReservationRepository @Inject constructor(
    private val local: IReservationLocalDatasource
) : IReservationRepository {
    override val reservations: Flow<List<ReservationEntity>>
        get() = local.reservations

    override suspend fun insertToDb(reservation: ReservationEntity): Long =
        local.insert(reservation)

    override suspend fun getById(id: Long): ReservationEntity = local.getById(id)

    override suspend fun deleteFromDb(reservation: ReservationEntity) = local.delete(reservation)

    /**
     * 💡If long running task are required we may indicate the dispatcher ie: IO.
     * Example:
     * suspend fun doJob() = withContext(Dispatchers.IO) { task1, task2, task3.. and so on}
     */
}