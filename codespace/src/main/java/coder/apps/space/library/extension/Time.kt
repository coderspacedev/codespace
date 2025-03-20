package coder.apps.space.library.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val instant = Instant.ofEpochMilli(this)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return dateTime.format(DateTimeFormatter.ofPattern(pattern))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
}

fun Long.timeDifferenceFrom(otherTime: Long, unit: TimeUnit = TimeUnit.MINUTES): Long {
    val diff = abs(this - otherTime)
    return unit.convert(diff, TimeUnit.MILLISECONDS)
}

fun Long.addTime(amount: Long, unit: TimeUnit): Long {
    return this + unit.toMillis(amount)
}

fun Long.subtractTime(amount: Long, unit: TimeUnit): Long {
    return this - unit.toMillis(amount)
}

fun Long.isInPast(): Boolean = this < System.currentTimeMillis()
fun Long.isInFuture(): Boolean = this > System.currentTimeMillis()

fun Long.toReadableDuration(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days days, ${hours % 24} hours"
        hours > 0 -> "$hours hours, ${minutes % 60} minutes"
        minutes > 0 -> "$minutes minutes, ${seconds % 60} seconds"
        else -> "$seconds seconds"
    }
}