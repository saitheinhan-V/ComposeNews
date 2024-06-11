package com.my.composenews.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

object DateConverterUtils {
    private const val WITHIN_ONE_DAY = "h:mm a"
    private const val WITHIN_ONE_WEEK = "EEE"
    private const val WITHIN_ONE_YEAR = "d MMMM"
    private const val OVER_ONE_YEAR = "d MMMM, yyyy"
    private const val SEVEN = 7
    private const val ONE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateTime(
        now: Long,
        input: Long,
        isForRoomList: Boolean = false,
        yesterday: String? = "",
        today: String? = ""
    ): String {
        val nowDateTime = getLocalDateTime(now)
        val inputDateTime = getLocalDateTime(input)
        val nowYear = nowDateTime.year
        val inputYear = inputDateTime.year

        val overOneYearFormat = DateTimeFormatter.ofPattern(OVER_ONE_YEAR)
        val withinOneYearFormat = DateTimeFormatter.ofPattern(WITHIN_ONE_YEAR)
        val withinOneWeekFormat = DateTimeFormatter.ofPattern(WITHIN_ONE_WEEK)
        val withinOneDayFormat = DateTimeFormatter.ofPattern(WITHIN_ONE_DAY)

        // check if over one year
        if (nowYear - inputYear >= 1) {
            return inputDateTime.toJavaLocalDateTime()
                .format(overOneYearFormat)
        } else {
            val nowDay = nowDateTime.dayOfMonth
            val inputDay = inputDateTime.dayOfMonth
            val nowMonth = nowDateTime.month
            val inputMonth = inputDateTime.month
            // check if within one day
            if (nowDay == inputDay && nowMonth == inputMonth) {
                return if (isForRoomList) {
                    inputDateTime.toJavaLocalDateTime()
                        .format(withinOneDayFormat)
                } else {
                    today!!
                }
            }
            // check if yesterday
            if (nowDay - inputDay == 1 && nowMonth == inputMonth) return yesterday!!
            // check if within one week
            val nowWeek = nowDateTime.dayOfYear
            val inputWeek = inputDateTime.dayOfYear
            if (nowWeek - inputWeek <= 7) {
                return inputDateTime.toJavaLocalDateTime().format(withinOneWeekFormat)
            }
            // within one year
            return inputDateTime.toJavaLocalDateTime().format(withinOneYearFormat)
        }
    }

    private fun getLocalDateTime(timestamp: Long): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }
}