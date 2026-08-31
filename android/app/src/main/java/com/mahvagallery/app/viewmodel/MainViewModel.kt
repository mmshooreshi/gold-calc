package com.mahvagallery.app.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mahvagallery.app.data.AppLogger
import com.mahvagallery.app.data.AppRepository
import com.mahvagallery.app.data.BackupSnapshot
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.CustomerInfo
import com.mahvagallery.app.model.DefaultValues
import com.mahvagallery.app.model.HistoryItem
import com.mahvagallery.app.model.LockSettings
import com.mahvagallery.app.model.LogEntry
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.PersianCalendarHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String) {
    CALCULATOR("محاسبه"),
    HISTORY("تاریخچه"),
    STATS("آمار"),
    SETTINGS("تنظیمات")
}

enum class HistoryFilter(val title: String) {
    ALL("همه"),
    SALES("فروش‌ها"),
    DRAFTS("پیش‌نویس‌ها"),
    TODAY("امروز")
}

enum class StatsDatePreset(val title: String) {
    ALL("همه"),
    TODAY("امروز"),
    WEEK("هفته"),
    MONTH("ماه"),
    THREE_MONTHS("۳ ماه")
}

enum class ChartType(val title: String) {
    BAR("ستونی"),
    LINE("خطی"),
    DOUGHNUT("دایره‌ای")
}

data class CalculatorState(
    val goldPrice: String = "",       // A
    val weight: String = "",          // B
    val ojratPercent: String = "",    // D
    val profitPercent: String = "",   // F
    val taxPercent: String = "",      // H

    val rawPrice: String = "",        // C = A * B
    val ojratAmount: String = "",     // E = D * C / 100
    val profitAmount: String = "",    // G = F * (C + E) / 100
    val taxAmount: String = "",       // I = (E + G) * H / 100
    val totalPrice: String = "",      // K = C + E + G + I
    val totalCosts: String = "",      // E + G + I
    val finalPercent: String = "",    // J = ((K/C) - 1) * 100

    val rawCalcData: CalcData = CalcData(),
    val editingId: Long? = null
)

data class InfoDialogData(
    val title: String,
    val description: String
)

data class ReceiptDialogData(
    val calcData: CalcData,
    val customer: CustomerInfo = CustomerInfo(),
    val date: String,
    val time: String,
    val title: String
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = AppRepository(application)

    private val _currentTab = MutableStateFlow(AppTab.CALCULATOR)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _calcState = MutableStateFlow(CalculatorState())
    val calcState: StateFlow<CalculatorState> = _calcState.asStateFlow()

    private val _activeCustomer = MutableStateFlow(CustomerInfo())
    val activeCustomer: StateFlow<CustomerInfo> = _activeCustomer.asStateFlow()

    private val _showCustomerDialog = MutableStateFlow(false)
    val showCustomerDialog: StateFlow<Boolean> = _showCustomerDialog.asStateFlow()

    val locks: StateFlow<LockSettings> = repository.locks
    val defaults: StateFlow<DefaultValues> = repository.defaults
    val isDarkMode: StateFlow<Boolean> = repository.isDarkMode
    val isBoldText: StateFlow<Boolean> = repository.isBoldText
    val fontScaleDelta: StateFlow<Int> = repository.fontScaleDelta
    val snapshots: StateFlow<List<BackupSnapshot>> = repository.snapshots
    val logs: StateFlow<List<LogEntry>> = AppLogger.logs

    private val _historyFilter = MutableStateFlow(HistoryFilter.ALL)
    val historyFilter: StateFlow<HistoryFilter> = _historyFilter.asStateFlow()

    private val _statsDatePreset = MutableStateFlow(StatsDatePreset.ALL)
    val statsDatePreset: StateFlow<StatsDatePreset> = _statsDatePreset.asStateFlow()

    private val _chartType = MutableStateFlow(ChartType.BAR)
    val chartType: StateFlow<ChartType> = _chartType.asStateFlow()

    // Active Dialogs
    private val _activeInfoDialog = MutableStateFlow<InfoDialogData?>(null)
    val activeInfoDialog: StateFlow<InfoDialogData?> = _activeInfoDialog.asStateFlow()

    private val _activeReceiptDialog = MutableStateFlow<ReceiptDialogData?>(null)
    val activeReceiptDialog: StateFlow<ReceiptDialogData?> = _activeReceiptDialog.asStateFlow()

    private val _itemToDelete = MutableStateFlow<Long?>(null)
    val itemToDelete: StateFlow<Long?> = _itemToDelete.asStateFlow()

    private val _showClearDataModal = MutableStateFlow(false)
    val showClearDataModal: StateFlow<Boolean> = _showClearDataModal.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Filtered history list
    val filteredHistory: StateFlow<List<HistoryItem>> = combine(
        repository.history,
        _historyFilter
    ) { history, filter ->
        when (filter) {
            HistoryFilter.ALL -> history
            HistoryFilter.SALES -> history.filter { it.type == "sale" }
            HistoryFilter.DRAFTS -> history.filter { it.type == "draft" }
            HistoryFilter.TODAY -> {
                val todayIso = PersianCalendarHelper.getCurrentIsoDate()
                history.filter { it.iso == todayIso }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        AppLogger.info("VM", "MainViewModel initialized")
        val lockedMap = repository.lockedValues.value
        val defaultVals = repository.defaults.value

        _calcState.update { state ->
            state.copy(
                goldPrice = lockedMap["A"] ?: "",
                ojratPercent = if (locks.value.lockD) lockedMap["D"] ?: "" else defaultVals.defaultD,
                profitPercent = if (locks.value.lockF) lockedMap["F"] ?: "" else defaultVals.defaultF,
                taxPercent = if (locks.value.lockH) lockedMap["H"] ?: "" else defaultVals.defaultH
            )
        }
        recalculate()
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
        AppLogger.debug("NAV", "Switched to tab: ${tab.name}")
    }

    fun setHistoryFilter(filter: HistoryFilter) {
        _historyFilter.value = filter
    }

    fun setStatsDatePreset(preset: StatsDatePreset) {
        _statsDatePreset.value = preset
    }

    fun setChartType(type: ChartType) {
        _chartType.value = type
    }

    fun openCustomerDialog() {
        _showCustomerDialog.value = true
    }

    fun closeCustomerDialog() {
        _showCustomerDialog.value = false
    }

    fun saveCustomerInfo(info: CustomerInfo) {
        _activeCustomer.value = info
        _showCustomerDialog.value = false
        if (info.isNotEmpty) {
            showToast("مشخصات مشتری ثبت شد: ${info.name}")
        }
    }

    fun onGoldPriceChange(value: String) {
        val formatted = NumberFormatters.formatInputWithCommas(value)
        _calcState.update { it.copy(goldPrice = formatted) }
        if (locks.value.lockA) repository.updateLockedValue("A", formatted)
        recalculate()
    }

    fun onWeightChange(value: String) {
        val formatted = NumberFormatters.formatDecimalInput(value)
        _calcState.update { it.copy(weight = formatted) }
        recalculate()
    }

    fun onOjratPercentChange(value: String) {
        val formatted = NumberFormatters.formatDecimalInput(value)
        _calcState.update { it.copy(ojratPercent = formatted) }
        if (locks.value.lockD) repository.updateLockedValue("D", formatted)
        recalculate()
    }

    fun onProfitPercentChange(value: String) {
        val formatted = NumberFormatters.formatDecimalInput(value)
        _calcState.update { it.copy(profitPercent = formatted) }
        if (locks.value.lockF) repository.updateLockedValue("F", formatted)
        recalculate()
    }

    fun onTaxPercentChange(value: String) {
        val formatted = NumberFormatters.formatDecimalInput(value)
        _calcState.update { it.copy(taxPercent = formatted) }
        if (locks.value.lockH) repository.updateLockedValue("H", formatted)
        recalculate()
    }

    fun toggleLock(field: String) {
        val value = when (field) {
            "A" -> _calcState.value.goldPrice
            "D" -> _calcState.value.ojratPercent
            "F" -> _calcState.value.profitPercent
            "H" -> _calcState.value.taxPercent
            else -> ""
        }
        repository.toggleLock(field, value)
    }

    private fun recalculate() {
        val s = _calcState.value
        val a = NumberFormatters.unformatNumber(s.goldPrice)
        val b = NumberFormatters.unformatNumber(s.weight)
        val d = NumberFormatters.unformatNumber(s.ojratPercent)
        val f = NumberFormatters.unformatNumber(s.profitPercent)
        val h = NumberFormatters.unformatNumber(s.taxPercent)

        if (a <= 0.0 || b <= 0.0) {
            _calcState.update {
                it.copy(
                    rawPrice = "",
                    ojratAmount = "",
                    profitAmount = "",
                    taxAmount = "",
                    totalPrice = "",
                    totalCosts = "",
                    finalPercent = "",
                    rawCalcData = CalcData()
                )
            }
            return
        }

        // C = A * B
        val c = a * b
        // E = D * C / 100
        val e = (d * c) / 100.0
        // G = F * (C + E) / 100 (Profit on raw + ojrat)
        val g = (f * (c + e)) / 100.0
        // I = (E + G) * H / 100 (Tax on ojrat + profit)
        val i = ((e + g) * h) / 100.0
        // K = C + E + G + I
        val k = c + e + g + i
        // Costs = E + G + I
        val costs = e + g + i
        // J = ((K / C) - 1) * 100
        val j = if (c > 0.0) ((k / c) - 1.0) * 100.0 else 0.0

        val calcData = CalcData(a, b, c, d, e, f, g, h, i, k, j, costs)

        _calcState.update {
            it.copy(
                rawPrice = NumberFormatters.formatCurrency(c),
                ojratAmount = NumberFormatters.formatCurrency(e),
                profitAmount = NumberFormatters.formatCurrency(g),
                taxAmount = NumberFormatters.formatCurrency(i),
                totalPrice = NumberFormatters.formatCurrency(k),
                totalCosts = NumberFormatters.formatCurrency(costs),
                finalPercent = NumberFormatters.formatPercentage(j),
                rawCalcData = calcData
            )
        }

        // Auto sync live draft if not editing existing record
        if (s.editingId == null) {
            repository.syncDraft(calcData, _activeCustomer.value)
        }
    }

    fun onClearForm(keepLocks: Boolean = true) {
        val l = locks.value
        val defs = repository.defaults.value

        _activeCustomer.value = CustomerInfo()
        _calcState.update {
            it.copy(
                goldPrice = if (keepLocks && l.lockA) it.goldPrice else "",
                weight = "",
                ojratPercent = if (keepLocks && l.lockD) it.ojratPercent else defs.defaultD,
                profitPercent = if (keepLocks && l.lockF) it.profitPercent else defs.defaultF,
                taxPercent = if (keepLocks && l.lockH) it.taxPercent else defs.defaultH
            )
        }
        recalculate()
        showToast("فرم پاک‌سازی شد")
    }

    fun onSaveSale() {
        val raw = _calcState.value.rawCalcData
        if (raw.k <= 0.0 || raw.b <= 0.0) {
            showToast("لطفاً ابتدا وزن و قیمت طلا را وارد کنید")
            return
        }
        val isEdit = _calcState.value.editingId != null
        repository.saveSale(raw, _activeCustomer.value, _calcState.value.editingId)
        
        if (isEdit) {
            cancelEdit()
            showToast("تراکنش بروزرسانی شد ✓")
        } else {
            onClearForm(keepLocks = true)
            showToast("فروش با موفقیت ثبت شد ✓")
        }
    }

    fun startEdit(item: HistoryItem) {
        _activeCustomer.value = item.customer
        _calcState.update {
            it.copy(
                goldPrice = NumberFormatters.formatCurrency(item.calc.a),
                weight = NumberFormatters.formatWeight(item.calc.b),
                ojratPercent = NumberFormatters.formatPercentage(item.calc.d),
                profitPercent = NumberFormatters.formatPercentage(item.calc.f),
                taxPercent = NumberFormatters.formatPercentage(item.calc.h),
                editingId = item.id
            )
        }
        recalculate()
        _currentTab.value = AppTab.CALCULATOR
        showToast("در حال ویرایش تراکنش")
    }

    fun cancelEdit() {
        _calcState.update { it.copy(editingId = null) }
        onClearForm(keepLocks = true)
    }

    fun openReceiptForCurrentForm() {
        val calc = _calcState.value.rawCalcData
        if (calc.k <= 0.0 || calc.b <= 0.0) {
            showToast("ابتدا مقادیر را وارد کنید")
            return
        }
        _activeReceiptDialog.value = ReceiptDialogData(
            calcData = calc,
            customer = _activeCustomer.value,
            date = PersianCalendarHelper.getTodayShamsi().formatPersian(),
            time = PersianCalendarHelper.getCurrentTimeString(),
            title = "پیش‌فاکتور طلا"
        )
    }

    fun openReceiptForHistory(item: HistoryItem) {
        _activeReceiptDialog.value = ReceiptDialogData(
            calcData = item.calc,
            customer = item.customer,
            date = item.date,
            time = item.time,
            title = if (item.type == "sale") "فاکتور فروش طلا" else "پیش‌فاکتور طلا"
        )
    }

    fun closeReceiptDialog() {
        _activeReceiptDialog.value = null
    }

    fun showInfo(title: String, description: String) {
        _activeInfoDialog.value = InfoDialogData(title, description)
    }

    fun closeInfoDialog() {
        _activeInfoDialog.value = null
    }

    fun requestDelete(item: HistoryItem) {
        _itemToDelete.value = item.id
    }

    fun confirmDelete() {
        val id = _itemToDelete.value ?: return
        repository.deleteTransaction(id)
        _itemToDelete.value = null
        showToast("تراکنش حذف شد")
    }

    fun cancelDelete() {
        _itemToDelete.value = null
    }

    fun requestClearAllData() {
        _showClearDataModal.value = true
    }

    fun confirmClearAllData() {
        repository.clearAllData()
        _showClearDataModal.value = false
        onClearForm(keepLocks = false)
        showToast("همه داده‌ها پاک شدند")
    }

    fun cancelClearAllData() {
        _showClearDataModal.value = false
    }

    fun toggleDarkMode() {
        repository.setDarkMode(!isDarkMode.value)
    }

    fun toggleBoldText() {
        repository.setBoldText(!isBoldText.value)
    }

    fun changeFontScale(delta: Int) {
        repository.setFontScaleDelta(fontScaleDelta.value + delta)
    }

    fun saveDefaultPercentages(defD: String, defF: String, defH: String) {
        repository.saveDefaults(defD, defF, defH)
        showToast("پیش‌فرض‌ها ذخیره شدند ✓")
    }

    fun restoreSnapshot(snapshotId: String) {
        val success = repository.restoreSnapshot(snapshotId)
        if (success) {
            showToast("نسخه پشتیبان با موفقیت بازیابی شد ✓")
        } else {
            showToast("خطا در بازیابی نسخه پشتیبان")
        }
    }

    fun importBackupFromUri(context: Context, uri: Uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val reader = BufferedReader(InputStreamReader(inputStream))
                val json = reader.use { it.readText() }
                val success = repository.importBackupJson(json)
                if (success) {
                    showToast("پشتیبان با موفقیت وارد و بازیابی شد ✓")
                } else {
                    showToast("قالب فایل JSON نامعتبر است")
                }
            }
        } catch (e: Exception) {
            showToast("خطا در باز کردن فایل: ${e.message}")
        }
    }

    fun copyLogsToClipboard(context: Context) {
        val logsText = AppLogger.getExportableLogText()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Mahva Debug Logs", logsText)
        clipboard.setPrimaryClip(clip)
        showToast("لاگ‌های دیباگ کپی شدند ✓")
    }

    fun clearDebugLogs() {
        AppLogger.clear()
        showToast("لاگ‌ها پاک‌سازی شدند")
    }

    fun showClearAllDataConfirmation() {
        _showClearDataModal.value = true
    }

    fun exportBackupJson(context: Context) {
        val json = repository.exportBackupJson()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_TEXT, json)
            putExtra(Intent.EXTRA_TITLE, "mahva-backup.json")
        }
        context.startActivity(Intent.createChooser(intent, "دانلود یا ارسال پشتیبان JSON"))
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
