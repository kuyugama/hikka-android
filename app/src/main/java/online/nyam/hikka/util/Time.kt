package online.nyam.hikka.util

import android.os.Build
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone
import kotlin.time.Duration.Companion.seconds

fun getYearFromTimestamp(timestamp: Long): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant
            .ofEpochSecond(timestamp)
            .atZone(ZoneId.of("UTC"))
            .year
    } else {
        val calendar =
            Calendar.getInstance().apply {
                timeZone = TimeZone.getTimeZone("UTC")
                timeInMillis = timestamp.seconds.inWholeMilliseconds
            }
        calendar.get(Calendar.YEAR)
    }
