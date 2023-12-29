package com.opentable.challenge.util

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


internal fun LocalDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("h:mm a"))
}

internal fun LocalDateTime.toMilliseconds(): Long {
    return this.toInstant(ZoneOffset.UTC).toEpochMilli()
}

internal fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}