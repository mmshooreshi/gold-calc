package com.mahvagallery.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object PersianCalendarHelper {

    val SHAMSI_MONTHS = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    data class ShamsiDate(val year: Int, val month: Int, val day: Int) {
        fun format(): String {
            return String.format(Locale.US, "%04d/%02d/%02d", year, month, day)
        }
        fun formatPersian(): String {
            return NumberFormatters.toPersianDigits(format())
        }
    }

    data class GregorianDate(val year: Int, val month: Int, val day: Int)

    fun gregorianToShamsi(gy: Int, gm: Int, gd: Int): ShamsiDate {
        val g = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        val gy2 = if (gm > 2) gy + 1 else gy
        var d = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + g[gm - 1]
        var jy = -1595 + (33 * (d / 12053))
        d %= 12053
        jy += 4 * (d / 1461)
        d %= 1461
        if (d > 365) {
            jy += (d - 1) / 365
            d = (d - 1) % 365
        }
        val jm: Int
        val jd: Int
        if (d < 186) {
            jm = 1 + (d / 31)
            jd = 1 + (d % 31)
        } else {
            d -= 186
            jm = 7 + (d / 30)
            jd = 1 + (d % 30)
        }
        return ShamsiDate(jy, jm, jd)
    }

    fun shamsiToGregorian(jy: Int, jm: Int, jd: Int): GregorianDate {
        val jy1 = jy + 1595
        var d = -355668 + (365 * jy1) + ((jy1 / 33) * 8) + (((jy1 % 33) + 3) / 4) + jd + (if (jm < 7) (jm - 1) * 31 else ((jm - 7) * 30) + 186)
        var gy = 400 * (d / 146097)
        d %= 146097
        if (d > 36524) {
            gy += 100 * (--d / 36524)
            d %= 36524
            if (d >= 365) d++
        }
        gy += 4 * (d / 1461)
        d %= 1461
        if (d > 365) {
            gy += (d - 1) / 365
            d = (d - 1) % 365
        }
        var gd = d + 1
        val isLeap = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        val m = intArrayOf(31, if (isLeap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 12 && gd > m[gm]) {
            gd -= m[gm]
            gm++
        }
        return GregorianDate(gy, gm + 1, gd)
    }

    fun isShamsiLeap(jy: Int): Boolean {
        val b = intArrayOf(1, 5, 9, 13, 17, 22, 26, 30)
        val c = ((jy - 474) % 2820 + 2820) % 2820 + 474
        return b.contains(c % 33)
    }

    fun getDaysInShamsiMonth(jy: Int, jm: Int): Int {
        return when {
            jm <= 6 -> 31
            jm <= 11 -> 30
            else -> if (isShamsiLeap(jy)) 30 else 29
        }
    }

    fun getTodayShamsi(): ShamsiDate {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"))
        return gregorianToShamsi(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getCurrentTimeString(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tehran")
        return sdf.format(Date())
    }

    fun getCurrentIsoDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }
}
