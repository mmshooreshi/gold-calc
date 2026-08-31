package com.mahvagallery.app

import com.mahvagallery.app.utils.NumberFormatters
import org.junit.Assert.assertEquals
import org.junit.Test

class NumberFormattersTest {

    @Test
    fun testNormalizePersianAndArabicDigits() {
        val persian = "۱۲۳۴۵۶۷۸۹۰"
        val arabic = "١٢٣٤٥٦٧٨٩٠"
        assertEquals("1234567890", NumberFormatters.toLatinDigits(persian))
        assertEquals("1234567890", NumberFormatters.toLatinDigits(arabic))
        assertEquals("۱۲۳۴۵۶۷۸۹۰", NumberFormatters.toPersianDigits("1234567890"))
    }

    @Test
    fun testUnformatNumber() {
        assertEquals(3500000.0, NumberFormatters.unformatNumber("۳,۵۰۰,۰۰۰"), 0.001)
        assertEquals(3500000.0, NumberFormatters.unformatNumber("3,500,000"), 0.001)
        assertEquals(4.25, NumberFormatters.unformatNumber("۴.۲۵"), 0.001)
        assertEquals(0.0, NumberFormatters.unformatNumber(""), 0.001)
        assertEquals(0.0, NumberFormatters.unformatNumber(null), 0.001)
    }

    @Test
    fun testFormatCurrency() {
        assertEquals("۳,۵۰۰,۰۰۰", NumberFormatters.formatCurrency(3500000.0))
        assertEquals("3,500,000", NumberFormatters.formatCurrency(3500000.0, toPersian = false))
        assertEquals("", NumberFormatters.formatCurrency(0.0))
    }

    @Test
    fun testFormatCurrencyInput() {
        assertEquals("۳,۵۰۰,۰۰۰", NumberFormatters.formatCurrencyInput("3500000"))
        assertEquals("۳,۵۰۰,۰۰۰", NumberFormatters.formatCurrencyInput("۳۵۰۰۰۰۰"))
    }

    @Test
    fun testFormatWeightInput() {
        assertEquals("۴.۲۵", NumberFormatters.formatWeightInput("4.25"))
        assertEquals("۴.۲۵", NumberFormatters.formatWeightInput("۴.۲۵"))
        assertEquals("۴.۲۵۰", NumberFormatters.formatWeightInput("4.2509")) // Truncates after 3 decimals
    }

    @Test
    fun testFormatPercentage() {
        assertEquals("۸.۱۶", NumberFormatters.formatPercentage(8.1612))
        assertEquals("8.16", NumberFormatters.formatPercentage(8.1612, toPersian = false))
    }
}
