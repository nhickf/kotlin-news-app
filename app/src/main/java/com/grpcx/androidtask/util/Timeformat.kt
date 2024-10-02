package com.grpcx.androidtask.util


import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.concurrent.TimeUnit

fun timeAgo(time: String): String? {
    val currentTime = Clock.System.now()
    val parsedTime = Instant.parse(time)
    if (parsedTime > currentTime) return null

    val timeElapsed = (currentTime - parsedTime).inWholeMilliseconds
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed)
    val hours = TimeUnit.MILLISECONDS.toHours(timeElapsed)
    val days = TimeUnit.MILLISECONDS.toDays(timeElapsed)

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        days < 30 -> "${days / 7} weeks ago"
        days < 365 -> "${days / 30} months ago"
        else -> "${days / 365} years ago"
    }
}