package com.mahvagallery.app.model

data class LockSettings(
    val lockA: Boolean = false,
    val lockD: Boolean = false,
    val lockF: Boolean = false,
    val lockH: Boolean = false
)

data class DefaultValues(
    val defaultD: String = "",
    val defaultF: String = "",
    val defaultH: String = ""
)

data class LogEntry(
    val timestamp: String = "",
    val tag: String = "APP",
    val message: String = "",
    val level: String = "INFO" // "INFO", "WARN", "DEBUG", "ERROR"
)
