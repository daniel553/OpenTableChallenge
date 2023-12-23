package com.opentable.data.reservation.repo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM Reservation ORDER BY reservation_time")
    fun all(): Flow<List<ReservationEntity>>

    /**
     * 💡The integration with Room and coroutines manages the way to access to db
     * with <b>suspend</b> keyword to not block UI thread.
     */
    @Query("SELECT * FROM Reservation WHERE id = :id")
    suspend fun getById(id: Long): ReservationEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservation: ReservationEntity): Long

    @Delete
    suspend fun delete(reservation: ReservationEntity)

}