package com.mahvagallery.app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.mahvagallery.app.utils.NumberFormatters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToLong

data class CalculatorUiState(
    val rawGoldPrice: String = "",       // val-A (قیمت طلای خام)
    val weight: String = "",             // val-B (وزن)
    val ojratPercent: String = "",       // val-D (درصد اجرت)
    val profitPercent: String = "",      // val-F (درصد سود)
    val taxPercent: String = "",         // val-H (درصد مالیات)
    
    val rawPrice: String = "",           // val-C (قیمت خام)
    val ojratAmount: String = "",        // val-E (مبلغ اجرت)
    val profitAmount: String = "",       // val-G (مبلغ سود)
    val taxAmount: String = "",          // val-I (مبلغ مالیات)
    val totalPrice: String = "",         // val-K (قیمت کل)
    val finalPercent: String = ""        // val-J (درصد نهایی)
)

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("mahva_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        // Load smart defaults from SharedPreferences (matching localStorage in PWA)
        val savedOjrat = prefs.getString("mahva_D", "") ?: ""
        val savedProfit = prefs.getString("mahva_F", "") ?: ""
        val savedTax = prefs.getString("mahva_H", "") ?: ""

        _uiState.update {
            it.copy(
                ojratPercent = savedOjrat,
                profitPercent = savedProfit,
                taxPercent = savedTax
            )
        }
    }

    fun onRawGoldPriceChange(input: String) {
        val formatted = NumberFormatters.formatCurrencyInput(input)
        _uiState.update { it.copy(rawGoldPrice = formatted) }
        recalculate()
    }

    fun onWeightChange(input: String) {
        val formatted = NumberFormatters.formatWeightInput(input)
        _uiState.update { it.copy(weight = formatted) }
        recalculate()
    }

    fun onOjratPercentChange(input: String) {
        val formatted = NumberFormatters.formatPercentageInput(input)
        prefs.edit().putString("mahva_D", formatted).apply()
        _uiState.update { it.copy(ojratPercent = formatted) }
        recalculate()
    }

    fun onProfitPercentChange(input: String) {
        val formatted = NumberFormatters.formatPercentageInput(input)
        prefs.edit().putString("mahva_F", formatted).apply()
        _uiState.update { it.copy(profitPercent = formatted) }
        recalculate()
    }

    fun onTaxPercentChange(input: String) {
        val formatted = NumberFormatters.formatPercentageInput(input)
        prefs.edit().putString("mahva_H", formatted).apply()
        _uiState.update { it.copy(taxPercent = formatted) }
        recalculate()
    }

    fun onReset() {
        _uiState.update {
            it.copy(
                rawGoldPrice = "",
                weight = "",
                rawPrice = "",
                ojratAmount = "",
                profitAmount = "",
                taxAmount = "",
                totalPrice = "",
                finalPercent = ""
            )
        }
        recalculate()
    }

    private fun recalculate() {
        val state = _uiState.value
        val a = NumberFormatters.unformatNumber(state.rawGoldPrice)
        val b = NumberFormatters.unformatNumber(state.weight)
        val d = NumberFormatters.unformatNumber(state.ojratPercent)
        val f = NumberFormatters.unformatNumber(state.profitPercent)
        val h = NumberFormatters.unformatNumber(state.taxPercent)

        if (a <= 0.0 || b <= 0.0) {
            _uiState.update {
                it.copy(
                    rawPrice = "",
                    ojratAmount = "",
                    profitAmount = "",
                    taxAmount = "",
                    totalPrice = "",
                    finalPercent = ""
                )
            }
            return
        }

        // C = A × B
        val c = a * b
        val cFormatted = NumberFormatters.formatCurrency(c.roundToLong())

        // E = D × C ÷ 100
        val e = (d * c) / 100.0
        val eFormatted = NumberFormatters.formatCurrency(e.roundToLong())

        // G = F × E ÷ 100
        val g = (f * e) / 100.0
        val gFormatted = NumberFormatters.formatCurrency(g.roundToLong())

        // I = (E + G) × H ÷ 100
        val i = ((e + g) * h) / 100.0
        val iFormatted = NumberFormatters.formatCurrency(i.roundToLong())

        // K = C + E + G + I
        val k = c + e + g + i
        val kFormatted = NumberFormatters.formatCurrency(k.roundToLong())

        // J = ((K ÷ C) - 1) × 100 (2 decimal places)
        val jFormatted = if (c > 0.0) {
            val j = ((k / c) - 1.0) * 100.0
            NumberFormatters.formatFinalPercentage(j)
        } else {
            ""
        }

        _uiState.update {
            it.copy(
                rawPrice = cFormatted,
                ojratAmount = eFormatted,
                profitAmount = gFormatted,
                taxAmount = iFormatted,
                totalPrice = kFormatted,
                finalPercent = jFormatted
            )
        }
    }
}
