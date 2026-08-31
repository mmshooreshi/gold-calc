package com.mahvagallery.app.data

import com.mahvagallery.app.model.LogEntry
import com.mahvagallery.app.utils.PersianCalendarHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppLogger {
    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    fun log(tag: String, message: String, level: String = "INFO") {
        val entry = LogEntry(
            timestamp = timeFormat.format(Date()),
            tag = tag,
            message = message,
            level = level
        )
        _logs.update { current ->
            val updated = current.toMutableList()
            updated.add(0, entry) // newest on top
            if (updated.size > 300) updated.take(300) else updated
        }
    }

    fun info(tag: String, message: String) = log(tag, message, "INFO")
    fun debug(tag: String, message: String) = log(tag, message, "DEBUG")
    fun warn(tag: String, message: String) = log(tag, message, "WARN")
    fun error(tag: String, message: String) = log(tag, message, "ERROR")

    fun clear() {
        _logs.value = emptyList()
        info("LOGGER", "Debug logs cleared by user")
    }

    fun getExportableLogText(): String {
        return _logs.value.joinToString("\n") {
            "[${it.timestamp}] [${it.level}] [${it.tag}]: ${it.message}"
        }
    }
}
