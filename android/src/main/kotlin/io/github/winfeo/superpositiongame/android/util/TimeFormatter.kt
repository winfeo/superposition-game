package io.github.winfeo.superpositiongame.android.util

import android.util.Log
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object TimeFormatter {
    private val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatTime(iso: String): String {
        return try {
            val dateTime = OffsetDateTime.parse(iso)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.d("TIME_FORMATTER", "Ошибка конфертации: ${e.message}")
            iso
        }
    }
}
