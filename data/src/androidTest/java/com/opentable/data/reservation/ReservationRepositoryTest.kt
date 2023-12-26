package com.opentable.data.reservation

import androidx.test.filters.SmallTest
import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.data.reservation.repo.ReservationLocalDatasource
import com.opentable.data.reservation.repo.ReservationRepository
import com.opentable.data.reservation.repo.db.ReservationEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@SmallTest
class ReservationRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @MockK
    lateinit var localDatasource: ReservationLocalDatasource

    private lateinit var repository: IReservationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        hiltRule.inject()
        repository = ReservationRepository(localDatasource)
    }

    @Before
    fun tearDown() {
        unmockkAll()
    }

    //💡The intention is to verify an internal call was performed (local datasource) insertion
    @Test
    fun givenReservation_whenRepositoryInsertsToDb_thenCallsLocalDatasourceInsert() = runTest {
        val reservation = ReservationEntity(name = "name", time = System.currentTimeMillis())

        repository.insertToDb(reservation)

        coVerify(atMost = 1) { localDatasource.insert(reservation) }
    }

    @Test
    fun givenReservationStored_whenGetReservation_thenCallsLocalDatasourceGetById() = runTest {
        val id = 1L
        val reservation = ReservationEntity(id, name = "name", time = System.currentTimeMillis())

        //Given a mock reservation object
        coEvery { localDatasource.getById(id) } returns reservation

        repository.getById(id)

        coVerify(atMost = 1) { localDatasource.getById(id) }

    }


}