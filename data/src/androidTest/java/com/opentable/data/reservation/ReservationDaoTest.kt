package com.opentable.data.reservation

import androidx.test.filters.SmallTest
import com.opentable.data.db.Database
import com.opentable.data.reservation.repo.db.ReservationDao
import com.opentable.data.reservation.repo.db.ReservationEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
internal class ReservationDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: Database

    //💡Some devs like to use subject as variable name of that is going to be tested in the suit
    // personally, I don't like it, I prefer to use a name similar to regular convention
    private lateinit var subject: ReservationDao

    @Before
    fun setUp() {
        hiltRule.inject()
        subject = db.reservationDao()
    }

    //💡yes, db can throw an io exception when attempting to close
    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun writeReservation_thenIdReservationIsReturned() = runTest {
        val reservation = ReservationEntity(name = "Test1", time = System.currentTimeMillis())

        val result = subject.insert(reservation)

        Assert.assertNotEquals(reservation.id, result)
    }

    @Test
    fun writeReservation_thenReadReservationEntity() = runTest {
        val reservation = ReservationEntity(name = "Test1", time = System.currentTimeMillis())

        val id = subject.insert(reservation)

        val result = subject.getById(id)

        Assert.assertEquals(reservation.name, result.name)
        Assert.assertEquals(reservation.time, result.time)
    }
}