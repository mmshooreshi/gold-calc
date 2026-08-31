package com.mahvagallery.app.model

data class EditTrace(
    val time: String = "",
    val date: String = "",
    val calc: CalcData = CalcData()
)

data class HistoryItem(
    val id: Long = System.currentTimeMillis(),
    val type: String = "sale",     // "sale" or "draft"
    val time: String = "",
    val date: String = "",
    val iso: String = "",
    val calc: CalcData = CalcData(),
    val edits: List<EditTrace> = emptyList()
)
