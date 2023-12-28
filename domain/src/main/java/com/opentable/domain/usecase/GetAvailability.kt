package com.opentable.domain.usecase

import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.domain.model.Reservation
import com.opentable.domain.util.toTimeString
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.floor

/**
 * 💡The restaurant is able to seat reservations between 5 PM and 11 PM and only at
 *   15-minute intervals.
 * - 5:10 PM is not a legal reservation time, but 5:15 PM and 5:30 PM are.
 * - If there is a reservation at 5pm then the times of 5:15, 5:30 and 5:45 would be
 *   unavailable.
 */
class GetAvailability @Inject constructor(
    private val reservationRepository: IReservationRepository
) {

    private val startTimeDefault = 17
    private val endTime = 24

    /**
     * Get availability list
     * @return a pair of String and Long
     */
    suspend operator fun invoke(): List<Pair<LocalDateTime, String>> {
        return coroutineScope {
            val reservationTimes = emptyList<Reservation>()
            // TODO: reservations data

            val dayTimes = createLocalDayTimes()

            dayTimes.removeIf { (time, str) ->
                inReservedTimeRange(time, time.plusHours(1), reservationTimes)
            }

            dayTimes.toList()
        }
    }

    private fun createLocalDayTimes(): MutableList<Pair<LocalDateTime, String>> {
        val dayTimes = mutableListOf<Pair<LocalDateTime, String>>()

        val now = LocalDateTime.now().withSecond(0).withNano(0) //ie: 18:35:00
        val start = LocalDateTime.now().withHour(startTimeDefault).withMinute(0) //ie: 17:00:00
            .withSecond(0).withNano(0)
        val end = LocalDateTime.now().withHour(endTime - 1).withMinute(59)//ie: 22:59:59.999999999
            .withSecond(59).withNano(999999999)

        if (now.isAfter(end)) return dayTimes //Just return an empty string

        var startTime = if (now.isAfter(start)) now else start

        val t =
            floor(startTime.minute / 15f).plus(1) // ie: 5:23 pm -> 23m/15 = 1.53 -> 2 (nd period)

        // ie: t=2 -> 2*15 -> 30 => 5:30 pm
        // ie: t=4 -> (5:55 pm) -> 6:00 pm
        startTime = if (t > 3) startTime.withMinute(0)
            .plusHours(1) else startTime.withMinute((t * 15).toInt())

        // Loop until the end time
        while (startTime.isBefore(end)) {
            dayTimes.add(
                Pair(
                    startTime,
                    startTime.toTimeString()
                )
            )
            startTime = startTime.plusMinutes(15)
        }

        return dayTimes
    }

    private fun inReservedTimeRange(
        start: LocalDateTime,
        end: LocalDateTime,
        reservations: List<Reservation>
    ): Boolean {
        return reservations.find { reservation ->
            reservation.time.isAfter(start) && reservation.time.isBefore(end)
        } != null
    }

}
