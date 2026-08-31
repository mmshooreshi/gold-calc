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
        assertEquals(3500000.0, NumberFormatters.unformatNumber("3,500,000"), 0.001)
        assertEquals(4.25, NumberFormatters.unformatNumber("4.25"), 0.001)
        assertEquals(0.0, NumberFormatters.unformatNumber(""), 0.001)
        assertEquals(0.0, NumberFormatters.unformatNumber(null), 0.001)
    }

    @Test
    fun testFormatCurrency() {
        assertEquals("3,500,000", NumberFormatters.formatCurrency(3500000.0))
        assertEquals("۳,۵۰۰,۰۰۰", NumberFormatters.formatCurrency(3500000.0, toPersian = true))
        assertEquals("", NumberFormatters.formatCurrency(0.0))
    }

    @Test
    fun testFormatCurrencyInput() {
        assertEquals("3,500,000", NumberFormatters.formatCurrencyInput("3500000"))
        assertEquals("3,500,000", NumberFormatters.formatCurrencyInput("۳۵۰۰۰۰۰"))
    }

    @Test
    fun testFormatWeightInput() {
        assertEquals("4.25", NumberFormatters.formatWeightInput("4.25"))
        assertEquals("4.25", NumberFormatters.formatWeightInput("۴.۲۵"))
        assertEquals("4.250", NumberFormatters.formatWeightInput("4.2509")) // Truncates after 3 decimals
    }

    @Test
    fun testFormatPercentage() {
        assertEquals("8.16", NumberFormatters.formatPercentage(8.1612))
        assertEquals("10.00", NumberFormatters.formatPercentage(10.0))
    }
}
