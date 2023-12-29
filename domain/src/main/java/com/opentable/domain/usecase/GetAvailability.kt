package com.opentable.domain.usecase

import com.opentable.data.reservation.repo.IReservationRepository
import com.opentable.domain.model.Reservation
import com.opentable.domain.model.toReservationList
import com.opentable.domain.util.toTimeString
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 💡The restaurant is able to seat reservations between 5 PM and 11 PM and only at
 *   15-minute intervals.
 * - 5:10 PM is not a legal reservation time, but 5:15 PM and 5:30 PM are.
 * - If there is a reservation at 5pm then the times of 5:15, 5:30 and 5:45 would be
 *   unavailable.
 */
class GetAvailability @Inject constructor(
    private val reservationRepository: IReservationRepository,
) {


    /**
     * Get availability list such as the date, the formatted date and if its available.
     * @param startTimeP starting by default 17 -> 5:00 PM
     * @param endTimeP ending by default 23 -> 11:00 PM (inclusive)
     * @param offsetP in hours for reservations -> 1 hour
     *
     * @return a triple of LocalDateTime, String and Boolean
     */
    suspend operator fun invoke(
        nowP: LocalDateTime = LocalDateTime.now(),
        startTimeP: Int = 17,
        endTimeP: Int = 23,
        offsetP: Int = 1
    ): List<Triple<LocalDateTime, String, Boolean>> {
        return coroutineScope {
            val reservationTimes = async {
                reservationRepository
                    .getReservationsByDate(LocalDate.now().toString())
            }.await().toReservationList()

            // Create today's availability
            createLocalDayTimes(reservationTimes, nowP, startTimeP, endTimeP, offsetP).toList()
        }
    }

    /**
     * Creates the today's availability with reservation time list
     */
    private fun createLocalDayTimes(
        reservationTimes: List<Reservation>,
        nowP: LocalDateTime,
        startTimeP: Int,
        endTimeP: Int,
        offsetP: Int
    ): List<Triple<LocalDateTime, String, Boolean>> {
        val dayTimes = mutableListOf<Triple<LocalDateTime, String, Boolean>>()

        val now = nowP.withSecond(0).withNano(0) //ie: 18:35:00
        val start = nowP.withHour(startTimeP).withMinute(0) //ie: 17:00:00
            .withSecond(0).withNano(0)
        var end = nowP.withHour(endTimeP - 1).withMinute(59)//ie: 22:59:59.999999999
            .withSecond(59).withNano(999999999)

        if (now.isAfter(end)) return dayTimes //Just return an empty string

        var startTime = if (now.isAfter(start)) now else start

        val t = (startTime.minute.toFloat() / 15f).toBigDecimal()
            .setScale(0, RoundingMode.UP).toInt() // ie: 5:23 pm -> 23m/15 = 1.53 -> 2 (nd period)

        // ie: t=2 -> 2*15 -> 30 => 5:30 pm
        // ie: t=4 -> (5:55 pm) -> 6:00 pm
        startTime = if (t > 3) startTime.withMinute(0).plusHours(1)
        else startTime.withMinute((t * 15))
        end = end.plusSeconds(1)

        // Loop until the end time
        while (startTime.isBefore(end)) {
            dayTimes.add(
                Triple(
                    startTime,
                    startTime.toTimeString(),
                    inReservedTimeRange(startTime, offsetP.toLong(), reservationTimes)
                )
            )
            startTime = startTime.plusMinutes(15)
        }

        return dayTimes.toList()
    }

    /**
     * 💡It check if the target hour is reserved in N offsetHours (1 hour by default)
     */
    private fun inReservedTimeRange(
        target: LocalDateTime,
        offsetHours: Long,
        reservations: List<Reservation>
    ): Boolean {
        reservations.forEach { reservation ->
            if (target.plusSeconds(1).isAfter(reservation.time) &&
                target.isBefore(reservation.time.plusHours(offsetHours))
            ) {
                return true
            }
        }
        return false
    }

}
