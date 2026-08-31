package com.mahvagallery.app

import com.mahvagallery.app.utils.NumberFormatters
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
        assertEquals("8,750,000", NumberFormatters.formatCurrency(c.roundToLong()))

        // E = D * C / 100
        val e = (d * c) / 100.0
        assertEquals(612500L, e.roundToLong())
        assertEquals("612,500", NumberFormatters.formatCurrency(e.roundToLong()))

        // G = F * E / 100
        val g = (f * e) / 100.0
        assertEquals(42875L, g.roundToLong())
        assertEquals("42,875", NumberFormatters.formatCurrency(g.roundToLong()))

        // I = (E + G) * H / 100
        val i = ((e + g) * h) / 100.0
        assertEquals(58984L, i.roundToLong())
        assertEquals("58,984", NumberFormatters.formatCurrency(i.roundToLong()))

        // K = C + E + G + I
        val k = c + e + g + i
        assertEquals(9464359L, k.roundToLong())
        assertEquals("9,464,359", NumberFormatters.formatCurrency(k.roundToLong()))

        // J = ((K / C) - 1) * 100
        val j = ((k / c) - 1.0) * 100.0
        assertEquals("8.16", NumberFormatters.formatFinalPercentage(j))
    }
}
