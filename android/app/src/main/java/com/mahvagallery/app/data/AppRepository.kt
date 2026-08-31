package com.mahvagallery.app.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.DefaultValues
import com.mahvagallery.app.model.EditTrace
import com.mahvagallery.app.model.HistoryItem
import com.mahvagallery.app.model.LockSettings
import com.mahvagallery.app.utils.PersianCalendarHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppRepository(context: Context) {

    private val prefs = context.getSharedPreferences("mahva_app_prefs_v6", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _locks = MutableStateFlow(LockSettings())
    val locks: StateFlow<LockSettings> = _locks.asStateFlow()

    private val _defaults = MutableStateFlow(DefaultValues())
    val defaults: StateFlow<DefaultValues> = _defaults.asStateFlow()

    private val _lockedValues = MutableStateFlow(mapOf<String, String>())
    val lockedValues: StateFlow<Map<String, String>> = _lockedValues.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isBoldText = MutableStateFlow(false)
    val isBoldText: StateFlow<Boolean> = _isBoldText.asStateFlow()

    private val _fontScaleDelta = MutableStateFlow(0)
    val fontScaleDelta: StateFlow<Int> = _fontScaleDelta.asStateFlow()

    init {
        loadPersistedData()
    }

    private fun loadPersistedData() {
        try {
            // Load history
            val historyJson = prefs.getString("history_v6", null)
            if (historyJson != null) {
                val type = object : TypeToken<List<HistoryItem>>() {}.type
                val items: List<HistoryItem> = gson.fromJson(historyJson, type) ?: emptyList()
                _history.value = items
            }

            // Load locks
            val lockA = prefs.getBoolean("lock_A", false)
            val lockD = prefs.getBoolean("lock_D", false)
            val lockF = prefs.getBoolean("lock_F", false)
            val lockH = prefs.getBoolean("lock_H", false)
            _locks.value = LockSettings(lockA, lockD, lockF, lockH)

            // Load locked values
            val valA = prefs.getString("locked_val_A", "") ?: ""
            val valD = prefs.getString("locked_val_D", "") ?: ""
            val valF = prefs.getString("locked_val_F", "") ?: ""
            val valH = prefs.getString("locked_val_H", "") ?: ""
            _lockedValues.value = mapOf("A" to valA, "D" to valD, "F" to valF, "H" to valH)

            // Load saved defaults
            val defD = prefs.getString("def_D", "") ?: ""
            val defF = prefs.getString("def_F", "") ?: ""
            val defH = prefs.getString("def_H", "") ?: ""
            _defaults.value = DefaultValues(defD, defF, defH)

            // Load settings
            _isDarkMode.value = prefs.getBoolean("dark_mode", false)
            _isBoldText.value = prefs.getBoolean("bold_text", false)
            _fontScaleDelta.value = prefs.getInt("font_scale_delta", 0)

            AppLogger.info("REPO", "Loaded ${_history.value.size} transactions and user settings from storage")
        } catch (e: Exception) {
            AppLogger.error("REPO", "Error loading storage: ${e.message}")
        }
    }

    private fun persistHistory() {
        try {
            val json = gson.toJson(_history.value)
            prefs.edit().putString("history_v6", json).apply()
        } catch (e: Exception) {
            AppLogger.error("REPO", "Failed to persist history: ${e.message}")
        }
    }

    fun saveSale(calcData: CalcData, editingId: Long? = null): HistoryItem {
        val today = PersianCalendarHelper.getTodayShamsi().formatPersian()
        val time = PersianCalendarHelper.getCurrentTimeString()
        val iso = PersianCalendarHelper.getCurrentIsoDate()

        if (editingId != null) {
            val existing = _history.value.find { it.id == editingId }
            if (existing != null) {
                val newTrace = EditTrace(existing.time, existing.date, existing.calc)
                val updatedList = _history.value.map { item ->
                    if (item.id == editingId) {
                        item.copy(
                            calc = calcData,
                            time = time,
                            date = today,
                            iso = iso,
                            type = "sale",
                            edits = listOf(newTrace) + item.edits
                        )
                    } else item
                }
                _history.value = updatedList
                persistHistory()
                AppLogger.info("REPO", "Updated transaction #$editingId with new total: ${calcData.k}")
                return updatedList.first { it.id == editingId }
            }
        }

        val newItem = HistoryItem(
            id = System.currentTimeMillis(),
            type = "sale",
            time = time,
            date = today,
            iso = iso,
            calc = calcData,
            edits = emptyList()
        )
        _history.update { current ->
            val list = listOf(newItem) + current
            if (list.size > 200) list.take(200) else list
        }
        persistHistory()
        AppLogger.info("REPO", "Created new sale transaction #${newItem.id}")
        return newItem
    }

    fun deleteTransaction(id: Long) {
        _history.update { current -> current.filter { it.id != id } }
        persistHistory()
        AppLogger.warn("REPO", "Deleted transaction #$id")
    }

    fun toggleLock(field: String, currentValue: String) {
        val current = _locks.value
        val newLocks = when (field) {
            "A" -> current.copy(lockA = !current.lockA)
            "D" -> current.copy(lockD = !current.lockD)
            "F" -> current.copy(lockF = !current.lockF)
            "H" -> current.copy(lockH = !current.lockH)
            else -> current
        }
        _locks.value = newLocks
        val isLocked = when (field) {
            "A" -> newLocks.lockA
            "D" -> newLocks.lockD
            "F" -> newLocks.lockF
            "H" -> newLocks.lockH
            else -> false
        }
        val lockedMap = _lockedValues.value.toMutableMap()
        lockedMap[field] = if (isLocked) currentValue else ""
        _lockedValues.value = lockedMap

        prefs.edit()
            .putBoolean("lock_$field", isLocked)
            .putString("locked_val_$field", lockedMap[field])
            .apply()

        AppLogger.debug("REPO", "Field $field lock state changed to $isLocked")
    }

    fun updateLockedValue(field: String, value: String) {
        val lockedMap = _lockedValues.value.toMutableMap()
        lockedMap[field] = value
        _lockedValues.value = lockedMap
        prefs.edit().putString("locked_val_$field", value).apply()
    }

    fun saveDefaults(defD: String, defF: String, defH: String) {
        _defaults.value = DefaultValues(defD, defF, defH)
        prefs.edit()
            .putString("def_D", defD)
            .putString("def_F", defF)
            .putString("def_H", defH)
            .apply()
        AppLogger.info("REPO", "Saved default percentages: D=$defD%, F=$defF%, H=$defH%")
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.edit().putBoolean("dark_mode", enabled).apply()
        AppLogger.debug("SETTINGS", "Dark mode set to $enabled")
    }

    fun setBoldText(enabled: Boolean) {
        _isBoldText.value = enabled
        prefs.edit().putBoolean("bold_text", enabled).apply()
        AppLogger.debug("SETTINGS", "Bold text set to $enabled")
    }

    fun setFontScaleDelta(delta: Int) {
        val clamped = delta.coerceIn(-3, 5)
        _fontScaleDelta.value = clamped
        prefs.edit().putInt("font_scale_delta", clamped).apply()
        AppLogger.debug("SETTINGS", "Font scale delta set to $clamped")
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
        _history.value = emptyList()
        _locks.value = LockSettings()
        _lockedValues.value = emptyMap()
        _defaults.value = DefaultValues()
        _isDarkMode.value = false
        _isBoldText.value = false
        _fontScaleDelta.value = 0
        AppLogger.warn("REPO", "All local application data cleared")
    }

    fun exportBackupJson(): String {
        val backupMap = mapOf(
            "history" to _history.value,
            "defaults" to _defaults.value,
            "locks" to _locks.value,
            "isDarkMode" to _isDarkMode.value,
            "isBoldText" to _isBoldText.value,
            "version" to "6.0.0"
        )
        return gson.toJson(backupMap)
    }

    fun importBackupJson(json: String): Boolean {
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val map: Map<String, Any> = gson.fromJson(json, type) ?: return false
            if (map.containsKey("history")) {
                val historyType = object : TypeToken<List<HistoryItem>>() {}.type
                val historyList: List<HistoryItem> = gson.fromJson(gson.toJson(map["history"]), historyType)
                _history.value = historyList
                persistHistory()
            }
            AppLogger.info("REPO", "Successfully imported JSON backup with ${_history.value.size} items")
            true
        } catch (e: Exception) {
            AppLogger.error("REPO", "Failed to import JSON: ${e.message}")
            false
        }
    }
}
