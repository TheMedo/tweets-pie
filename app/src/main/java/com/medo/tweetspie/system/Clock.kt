package com.medo.tweetspie.system

import java.util.Calendar
import java.util.concurrent.TimeUnit

interface Clock {

    fun getCurrentTime(): Long

    fun daysToMillis(days: Long): Long

    fun timezoneOffset(calendar: Calendar): Long
}

class ClockImpl : Clock {

    override fun getCurrentTime() = System.currentTimeMillis()

    override fun daysToMillis(days: Long) = TimeUnit.DAYS.toMillis(days)

    override fun timezoneOffset(calendar: Calendar): Long {
        val now = Calendar.getInstance()
        val nowOffset = now.get(Calendar.ZONE_OFFSET) + now.get(Calendar.DST_OFFSET)
        val calendarOffset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
        return Math.abs(TimeUnit.MILLISECONDS.toHours((nowOffset - calendarOffset).toLong()))
    }
}