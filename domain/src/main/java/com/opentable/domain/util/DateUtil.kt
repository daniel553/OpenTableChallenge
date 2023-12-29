package com.opentable.domain.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun LocalDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("h:mm a"))
}

internal fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this)
}