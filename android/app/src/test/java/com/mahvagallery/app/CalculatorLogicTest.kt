package com.mahvagallery.app

import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.PersianCalendarHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToLong

class CalculatorLogicTest {

    @Test
    fun testExactFormulaParityWithPWA() {
        // Given inputs:
        // A: Raw Gold Price = 3,500,000 Tomans
        // B: Weight = 2.5 grams
        // D: Ojrat = 7%
        // F: Profit = 7%
        // H: Tax = 9%
        val a = 3500000.0
        val b = 2.5
        val d = 7.0
        val f = 7.0
        val h = 9.0

        // C = A * B
        val c = a * b
        assertEquals(8750000L, c.roundToLong())
        assertEquals("8,750,000", NumberFormatters.formatCurrency(c))

        // E = D * C / 100
        val e = (d * c) / 100.0
        assertEquals(612500L, e.roundToLong())
        assertEquals("612,500", NumberFormatters.formatCurrency(e))

        // G = F * (C + E) / 100 (Profit on raw gold + ojrat)
        val g = (f * (c + e)) / 100.0
        assertEquals(655375L, g.roundToLong())
        assertEquals("655,375", NumberFormatters.formatCurrency(g))

        // I = (E + G) * H / 100 (Tax on ojrat + profit)
        val i = ((e + g) * h) / 100.0
        assertEquals(114109L, i.roundToLong())
        assertEquals("114,109", NumberFormatters.formatCurrency(i))

        // K = C + E + G + I
        val k = c + e + g + i
        assertEquals(10131984L, k.roundToLong())
        assertEquals("10,131,984", NumberFormatters.formatCurrency(k))

        // Total Costs = E + G + I
        val costs = e + g + i
        assertEquals(1381984L, costs.roundToLong())

        // J = ((K / C) - 1) * 100
        val j = ((k / c) - 1.0) * 100.0
        assertEquals("15.79", NumberFormatters.formatPercentage(j))
    }

    @Test
    fun testShamsiCalendarConversion() {
        val shamsi = PersianCalendarHelper.gregorianToShamsi(2024, 3, 20)
        assertEquals(1403, shamsi.year)
        assertEquals(1, shamsi.month)
        assertEquals(1, shamsi.day)

        val gregorian = PersianCalendarHelper.shamsiToGregorian(1403, 1, 1)
        assertEquals(2024, gregorian.year)
        assertEquals(3, gregorian.month)
        assertEquals(20, gregorian.day)
    }
}
