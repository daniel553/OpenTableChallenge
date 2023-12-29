package com.opentable.domain.usecase

import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.data.reservation.repo.ReservationRepository
import com.opentable.data.reservation.repo.db.ReservationEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

/**
 * 💡Unit test to verify common and edgy cases in get availability use case
 */
internal class GetAvailabilityTest {

    @MockK
    lateinit var reservationRepository: IReservationRepository

    private lateinit var subject: GetAvailability

    @Before
    fun setUp() {
        reservationRepository = mockk<ReservationRepository>(relaxed = true)
        subject = GetAvailability(reservationRepository)
    }

    @Test
    fun `given no reservations, when getting availability, then today's full availability is returned`() =
        runTest {
            coEvery { reservationRepository.getReservationsByDate(any()) } returns emptyList()
            val availability =
                subject.invoke(nowP = LocalDateTime.now().withHour(0).withMinute(0)) // 0:00 AM

            availability.forEach { (time, formatted, _) ->
                Assert.assertTrue(time.toString().length >= 15)
                Assert.assertTrue(formatted.length >= 7)
            }
        }


    @Test
    fun `given no reservations, when getting availability after hours, then return empty availability `() =
        runTest {
            coEvery { reservationRepository.getReservationsByDate(any()) } returns emptyList()

            val availability =
                subject.invoke(nowP = LocalDateTime.now().withHour(23).withMinute(50)) // 11:50pm

            Assert.assertEquals(0, availability.size)
        }


    @Test
    fun `given reservations, when getting availability, then reservation repository calls reservations by date`() =
        runTest {
            subject.invoke()

            coVerify(atLeast = 1) { reservationRepository.getReservationsByDate(any()) }
        }


    @Test
    fun `given reservation, when getting availability, then return availability with scoped hours`() =
        runTest {
            //5:30 PM on 12/28, to 6:30 PM in default scoped hour
            val reservationTime = "2023-12-28T17:30"
            val nowTime = "2023-12-28T16:00"
            coEvery { reservationRepository.getReservationsByDate(any()) } returns listOf(
                ReservationEntity(1L, "Name", reservationTime)
            )

            val availability = subject.invoke(nowP = LocalDateTime.parse(nowTime))

            //Before
            availability.first().let { (time, _, reserved) ->
                val rTime = LocalDateTime.parse(reservationTime)
                Assert.assertTrue(rTime.isAfter(time))
                Assert.assertFalse(reserved)
            }

            //On Reservation
            availability.component3().let { (time, _, reserved) ->
                Assert.assertEquals(reservationTime, time.toString())
                Assert.assertTrue(reserved)
            }

            //After
            availability.last().let { (time, _, reserved) ->
                val rTime = LocalDateTime.parse(reservationTime)
                Assert.assertTrue(rTime.isBefore(time))
                Assert.assertFalse(reserved)
            }

        }

}