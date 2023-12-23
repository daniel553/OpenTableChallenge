package com.opentable.data.reservation

import com.opentable.data.db.Database
import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.data.reservation.repo.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 💡Binds and provides must be in separate classes
 */
@Module
@InstallIn(SingletonComponent::class)
object ReservationModuleProvider {
    @Provides
    @Singleton
    fun provideReservationDao(db: Database) = db.reservationDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ReservationModuleBinder {
    @Binds
    @Singleton
    abstract fun bindReservationRepository(repo: ReservationRepository): IReservationRepository

    @Binds
    @Singleton
    abstract fun bindReservationLocalDatasource(datasource: ReservationRepository): IReservationRepository
}