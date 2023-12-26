package com.opentable.challenge.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Long.toTimeString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.format(date)
}

fun String.dateToLong(): Long = try {
    val df = SimpleDateFormat("HH:mm", Locale.US)
    df.parse(this)?.time ?: 0L
} catch (exception: Exception) {
    0L
}