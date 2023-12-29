package com.opentable.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.opentable.data.reservation.repo.db.ReservationDao
import com.opentable.data.reservation.repo.db.ReservationEntity

/**
 * 💡Single database to work offline
 */
@Database(entities = [ReservationEntity::class], version = 2)
abstract class Database : RoomDatabase() {
    companion object {
        const val name = "opentable-challenge.db"
    }

    // DAO definitions --------------------
    abstract fun reservationDao(): ReservationDao
}