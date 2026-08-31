package com.mahvagallery.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.CustomerInfo
import com.mahvagallery.app.model.DefaultValues
import com.mahvagallery.app.model.EditTrace
import com.mahvagallery.app.model.HistoryItem
import com.mahvagallery.app.model.LockSettings
import com.mahvagallery.app.utils.PersianCalendarHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BackupSnapshot(
    val id: String = System.currentTimeMillis().toString(),
    val date: String = "",
    val time: String = "",
    val itemCount: Int = 0,
    val isCurrent: Boolean = false,
    val jsonPayload: String = ""
)

class AppRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("mahva_gold_calc_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _locks = MutableStateFlow(LockSettings())
    val locks: StateFlow<LockSettings> = _locks.asStateFlow()

    private val _lockedValues = MutableStateFlow<Map<String, String>>(emptyMap())
    val lockedValues: StateFlow<Map<String, String>> = _lockedValues.asStateFlow()

    private val _defaults = MutableStateFlow(DefaultValues())
    val defaults: StateFlow<DefaultValues> = _defaults.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isBoldText = MutableStateFlow(false)
    val isBoldText: StateFlow<Boolean> = _isBoldText.asStateFlow()

    private val _fontScaleDelta = MutableStateFlow(0)
    val fontScaleDelta: StateFlow<Int> = _fontScaleDelta.asStateFlow()

    private val _snapshots = MutableStateFlow<List<BackupSnapshot>>(emptyList())
    val snapshots: StateFlow<List<BackupSnapshot>> = _snapshots.asStateFlow()

    private val _isPasscodeEnabled = MutableStateFlow(false)
    val isPasscodeEnabled: StateFlow<Boolean> = _isPasscodeEnabled.asStateFlow()

    private val _passcodePin = MutableStateFlow("")
    val passcodePin: StateFlow<String> = _passcodePin.asStateFlow()

    private val _isAppUnlocked = MutableStateFlow(true)
    val isAppUnlocked: StateFlow<Boolean> = _isAppUnlocked.asStateFlow()

    init {
        loadDataFromStorage()
    }

    private fun loadDataFromStorage() {
        try {
            // Load history
            val historyJson = prefs.getString("history_v6", null)
            if (!historyJson.isNullOrEmpty()) {
                val listType = object : TypeToken<List<HistoryItem>>() {}.type
                val loadedList: List<HistoryItem> = gson.fromJson(historyJson, listType) ?: emptyList()
                _history.value = loadedList
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

            // Load Passcode
            val isPinOn = prefs.getBoolean("passcode_enabled", false)
            val pin = prefs.getString("passcode_pin", "") ?: ""
            _isPasscodeEnabled.value = isPinOn && pin.isNotEmpty()
            _passcodePin.value = pin
            _isAppUnlocked.value = !isPinOn || pin.isEmpty()

            // Load snapshots
            val snapJson = prefs.getString("snapshots_v1", null)
            if (!snapJson.isNullOrEmpty()) {
                val snapType = object : TypeToken<List<BackupSnapshot>>() {}.type
                val loadedSnapshots: List<BackupSnapshot> = gson.fromJson(snapJson, snapType) ?: emptyList()
                _snapshots.value = loadedSnapshots
            } else {
                createInitialSnapshot()
            }

            AppLogger.info("REPO", "Loaded ${_history.value.size} transactions, passcode & snapshots from storage")
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

    private fun persistSnapshots() {
        try {
            val json = gson.toJson(_snapshots.value)
            prefs.edit().putString("snapshots_v1", json).apply()
        } catch (e: Exception) {
            AppLogger.error("REPO", "Failed to persist snapshots: ${e.message}")
        }
    }

    fun setPasscode(pin: String) {
        _passcodePin.value = pin
        _isPasscodeEnabled.value = true
        _isAppUnlocked.value = true
        prefs.edit()
            .putString("passcode_pin", pin)
            .putBoolean("passcode_enabled", true)
            .apply()
        AppLogger.info("AUTH", "Passcode enabled")
    }

    fun disablePasscode() {
        _isPasscodeEnabled.value = false
        _passcodePin.value = ""
        _isAppUnlocked.value = true
        prefs.edit()
            .remove("passcode_pin")
            .putBoolean("passcode_enabled", false)
            .apply()
        AppLogger.info("AUTH", "Passcode disabled")
    }

    fun unlockApp() {
        _isAppUnlocked.value = true
    }

    fun lockApp() {
        if (_isPasscodeEnabled.value) {
            _isAppUnlocked.value = false
        }
    }

    fun createInitialSnapshot() {
        val payload = exportBackupJson()
        val snap = BackupSnapshot(
            id = "init",
            date = PersianCalendarHelper.getTodayShamsi().formatPersian(),
            time = PersianCalendarHelper.getCurrentTimeString(),
            itemCount = _history.value.size,
            isCurrent = true,
            jsonPayload = payload
        )
        _snapshots.value = listOf(snap)
        persistSnapshots()
    }

    fun autoUpdateCurrentSnapshot() {
        val payload = exportBackupJson()
        val currentSnap = _snapshots.value.firstOrNull { it.isCurrent }
        if (currentSnap != null) {
            val updatedSnap = currentSnap.copy(
                date = PersianCalendarHelper.getTodayShamsi().formatPersian(),
                time = PersianCalendarHelper.getCurrentTimeString(),
                itemCount = _history.value.size,
                jsonPayload = payload
            )
            _snapshots.value = _snapshots.value.map { if (it.id == currentSnap.id) updatedSnap else it }
        } else {
            createSnapshot()
        }
        persistSnapshots()
    }

    fun createSnapshot(): BackupSnapshot {
        val payload = exportBackupJson()
        val snap = BackupSnapshot(
            id = System.currentTimeMillis().toString(),
            date = PersianCalendarHelper.getTodayShamsi().formatPersian(),
            time = PersianCalendarHelper.getCurrentTimeString(),
            itemCount = _history.value.size,
            isCurrent = true,
            jsonPayload = payload
        )
        val updated = listOf(snap) + _snapshots.value.map { it.copy(isCurrent = false) }
        _snapshots.value = if (updated.size > 8) updated.take(8) else updated
        persistSnapshots()
        AppLogger.info("REPO", "Created backup snapshot #${snap.id}")
        return snap
    }

    fun restoreSnapshot(snapshotId: String): Boolean {
        val snap = _snapshots.value.find { it.id == snapshotId } ?: return false
        val success = importBackupJson(snap.jsonPayload)
        if (success) {
            _snapshots.update { list ->
                list.map { it.copy(isCurrent = it.id == snapshotId) }
            }
            persistSnapshots()
        }
        return success
    }

    fun saveSale(calcData: CalcData, customer: CustomerInfo = CustomerInfo(), editingId: Long? = null): HistoryItem {
        val today = PersianCalendarHelper.getTodayShamsi().formatPersian()
        val time = PersianCalendarHelper.getCurrentTimeString()
        val iso = PersianCalendarHelper.getCurrentIsoDate()

        // Remove active draft if any
        val withoutDrafts = _history.value.filter { it.type != "draft" || it.id == editingId }

        if (editingId != null) {
            val existing = _history.value.find { it.id == editingId }
            if (existing != null) {
                val newTrace = EditTrace(existing.time, existing.date, existing.calc, existing.customer)
                val updatedList = withoutDrafts.map { item ->
                    if (item.id == editingId) {
                        item.copy(
                            calc = calcData,
                            customer = customer,
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
            customer = customer,
            edits = emptyList()
        )
        _history.update { current ->
            val list = listOf(newItem) + current.filter { it.type != "draft" }
            if (list.size > 200) list.take(200) else list
        }
        persistHistory()
        AppLogger.info("REPO", "Created new sale transaction #${newItem.id}")
        return newItem
    }

    fun syncDraft(calcData: CalcData, customer: CustomerInfo = CustomerInfo()) {
        if (calcData.b <= 0.0 && calcData.a <= 0.0) return

        val today = PersianCalendarHelper.getTodayShamsi().formatPersian()
        val time = PersianCalendarHelper.getCurrentTimeString()
        val iso = PersianCalendarHelper.getCurrentIsoDate()

        val currentList = _history.value
        val existingDraft = currentList.firstOrNull { it.type == "draft" }

        val draftItem = if (existingDraft != null) {
            existingDraft.copy(
                calc = calcData,
                customer = customer,
                time = time,
                date = today,
                iso = iso
            )
        } else {
            HistoryItem(
                id = 9999999999L,
                type = "draft",
                time = time,
                date = today,
                iso = iso,
                calc = calcData,
                customer = customer,
                edits = emptyList()
            )
        }

        _history.value = listOf(draftItem) + currentList.filter { it.type != "draft" }
        persistHistory()
    }

    fun deleteTransaction(id: Long) {
        _history.update { current ->
            current.filter { it.id != id }
        }
        persistHistory()
        AppLogger.info("REPO", "Deleted transaction #$id")
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

        // Sync with default values so settings defaults and locks are unified
        if (isLocked) {
            when (field) {
                "D" -> saveDefaults(currentValue, _defaults.value.defaultF, _defaults.value.defaultH)
                "F" -> saveDefaults(_defaults.value.defaultD, currentValue, _defaults.value.defaultH)
                "H" -> saveDefaults(_defaults.value.defaultD, _defaults.value.defaultF, currentValue)
            }
        }

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
        val clamped = delta.coerceIn(-4, 10)
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
        _snapshots.value = emptyList()
        _isPasscodeEnabled.value = false
        _passcodePin.value = ""
        _isAppUnlocked.value = true
        AppLogger.warn("REPO", "All local application data cleared")
    }

    fun exportBackupJson(): String {
        val backupMap = mapOf(
            "history" to _history.value,
            "defaults" to _defaults.value,
            "locks" to _locks.value,
            "isDarkMode" to _isDarkMode.value,
            "isBoldText" to _isBoldText.value,
            "version" to "2.0.0"
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
            if (map.containsKey("defaults")) {
                val defType = object : TypeToken<DefaultValues>() {}.type
                val defs: DefaultValues = gson.fromJson(gson.toJson(map["defaults"]), defType)
                saveDefaults(defs.defaultD, defs.defaultF, defs.defaultH)
            }
            createSnapshot()
            AppLogger.info("REPO", "Successfully imported JSON backup with ${_history.value.size} items")
            true
        } catch (e: Exception) {
            AppLogger.error("REPO", "Failed to import JSON: ${e.message}")
            false
        }
    }
}
