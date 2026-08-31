package com.mahvagallery.app.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatters {

    private val decimalSymbols = DecimalFormatSymbols(Locale.US)
    private val currencyFormatter = DecimalFormat("#,###", decimalSymbols)
    private val percentageFormatter = DecimalFormat("0.00", decimalSymbols)

    /**
     * Converts Persian and Arabic digits to Latin digits (0-9).
     */
    fun normalizeDigits(input: String): String {
        return input
            .replace('۰', '0')
            .replace('۱', '1')
            .replace('۲', '2')
            .replace('۳', '3')
            .replace('۴', '4')
            .replace('۵', '5')
            .replace('۶', '6')
            .replace('۷', '7')
            .replace('۸', '8')
            .replace('۹', '9')
            .replace('٠', '0')
            .replace('١', '1')
            .replace('٢', '2')
            .replace('٣', '3')
            .replace('٤', '4')
            .replace('٥', '5')
            .replace('٦', '6')
            .replace('٧', '7')
            .replace('٨', '8')
            .replace('٩', '9')
            .replace('٫', '.')
            .replace('،', ',')
    }

    /**
     * Strips commas and parses to Double. Returns 0.0 on empty or invalid string.
     */
    fun unformatNumber(input: String?): Double {
        if (input.isNullOrBlank()) return 0.0
        val normalized = normalizeDigits(input).replace(",", "").trim()
        return normalized.toDoubleOrNull() ?: 0.0
    }

    /**
     * Formats a number with comma separators (e.g. 1,000,000).
     */
    fun formatCurrency(amount: Double): String {
        if (amount == 0.0) return ""
        val rounded = Math.round(amount)
        return currencyFormatter.format(rounded)
    }

    fun formatCurrency(amount: Long): String {
        if (amount == 0L) return ""
        return currencyFormatter.format(amount)
    }

    /**
     * Formats live currency input while typing (strips commas, adds commas).
     */
    fun formatCurrencyInput(input: String): String {
        val normalized = normalizeDigits(input).replace("[^0-9]".toRegex(), "")
        if (normalized.isEmpty()) return ""
        val num = normalized.toLongOrNull() ?: return ""
        return currencyFormatter.format(num)
    }

    /**
     * Formats weight input with up to 3 decimal places.
     */
    fun formatWeightInput(input: String): String {
        val normalized = normalizeDigits(input).replace("[^0-9.]".toRegex(), "")
        if (normalized.isEmpty()) return ""
        
        // If there are multiple dots, keep only the first
        val firstDotIndex = normalized.indexOf('.')
        if (firstDotIndex != -1) {
            val integerPart = normalized.substring(0, firstDotIndex)
            val rest = normalized.substring(firstDotIndex + 1).replace(".", "")
            val decimalPart = if (rest.length > 3) rest.substring(0, 3) else rest
            return "$integerPart.$decimalPart"
        }
        return normalized
    }

    /**
     * Formats percentage input (numbers and optional single decimal point).
     */
    fun formatPercentageInput(input: String): String {
        val normalized = normalizeDigits(input).replace("[^0-9.]".toRegex(), "")
        if (normalized.isEmpty()) return ""
        
        val firstDotIndex = normalized.indexOf('.')
        if (firstDotIndex != -1) {
            val integerPart = normalized.substring(0, firstDotIndex)
            val decimalPart = normalized.substring(firstDotIndex + 1).replace(".", "")
            return "$integerPart.$decimalPart"
        }
        return normalized
    }

    /**
     * Formats final percentage to 2 decimal places (e.g. 8.16).
     */
    fun formatFinalPercentage(value: Double): String {
        return percentageFormatter.format(value)
    }
}
