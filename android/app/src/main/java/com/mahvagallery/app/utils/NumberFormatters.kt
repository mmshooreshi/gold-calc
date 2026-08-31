package com.mahvagallery.app.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatters {

    private val decimalSymbols = DecimalFormatSymbols(Locale.US)
    private val currencyFormatter = DecimalFormat("#,###", decimalSymbols)
    private val weightFormatter = DecimalFormat("0.000", decimalSymbols)
    private val percentageFormatter = DecimalFormat("0.00", decimalSymbols)

    fun toPersianDigits(input: String): String {
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val chars = input.toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (c in '0'..'9') {
                chars[i] = persianDigits[c - '0']
            }
        }
        return String(chars)
    }

    fun toLatinDigits(input: String): String {
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

    fun unformatNumber(input: String?): Double {
        if (input.isNullOrBlank()) return 0.0
        val normalized = toLatinDigits(input).replace(",", "").trim()
        return normalized.toDoubleOrNull() ?: 0.0
    }

    fun formatCurrency(amount: Double, toPersian: Boolean = true): String {
        if (amount <= 0.0) return ""
        val formatted = currencyFormatter.format(Math.round(amount))
        return if (toPersian) toPersianDigits(formatted) else formatted
    }

    fun formatWeight(amount: Double, toPersian: Boolean = true): String {
        if (amount <= 0.0) return ""
        val formatted = weightFormatter.format(amount)
        return if (toPersian) toPersianDigits(formatted) else formatted
    }

    fun formatPercentage(amount: Double, toPersian: Boolean = true): String {
        if (amount <= 0.0) return ""
        val formatted = percentageFormatter.format(amount)
        return if (toPersian) toPersianDigits(formatted) else formatted
    }

    fun formatCurrencyInput(input: String): String {
        val latin = toLatinDigits(input).replace("[^0-9]".toRegex(), "")
        if (latin.isEmpty()) return ""
        val num = latin.toLongOrNull() ?: return ""
        val formatted = currencyFormatter.format(num)
        return toPersianDigits(formatted)
    }

    fun formatInputWithCommas(input: String): String = formatCurrencyInput(input)

    fun formatWeightInput(input: String): String {
        val latin = toLatinDigits(input).replace("[^0-9.]".toRegex(), "")
        if (latin.isEmpty()) return ""
        val firstDotIndex = latin.indexOf('.')
        val result = if (firstDotIndex != -1) {
            val integerPart = latin.substring(0, firstDotIndex)
            val rest = latin.substring(firstDotIndex + 1).replace(".", "")
            val decimalPart = if (rest.length > 3) rest.substring(0, 3) else rest
            "$integerPart.$decimalPart"
        } else {
            latin
        }
        return toPersianDigits(result)
    }

    fun formatDecimalInput(input: String): String = formatWeightInput(input)

    fun formatPercentageInput(input: String): String {
        val latin = toLatinDigits(input).replace("[^0-9.]".toRegex(), "")
        if (latin.isEmpty()) return ""
        val firstDotIndex = latin.indexOf('.')
        val result = if (firstDotIndex != -1) {
            val integerPart = latin.substring(0, firstDotIndex)
            val decimalPart = latin.substring(firstDotIndex + 1).replace(".", "")
            val clamped = if (decimalPart.length > 2) decimalPart.substring(0, 2) else decimalPart
            "$integerPart.$clamped"
        } else {
            latin
        }
        return toPersianDigits(result)
    }

    fun calculateCursorPosition(
        newRawText: String,
        rawCursor: Int,
        formattedText: String
    ): Int {
        val unformattedBeforeCursor = newRawText
            .take(rawCursor.coerceIn(0, newRawText.length))
            .count { it.isDigit() || it in '۰'..'۹' || it == '.' || it == '٫' }

        if (unformattedBeforeCursor == 0) return 0

        var count = 0
        for (i in formattedText.indices) {
            val c = formattedText[i]
            if (c.isDigit() || c in '۰'..'۹' || c == '.' || c == '٫') {
                count++
                if (count == unformattedBeforeCursor) {
                    return i + 1
                }
            }
        }
        return formattedText.length
    }
}
