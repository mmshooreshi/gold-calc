package com.mahvagallery.app.model

data class EditTrace(
    val time: String = "",
    val date: String = "",
    val calc: CalcData = CalcData(),
    val customer: CustomerInfo = CustomerInfo()
)

data class HistoryItem(
    val id: Long = System.currentTimeMillis(),
    val type: String = "sale",     // "sale" or "draft"
    val time: String = "",
    val date: String = "",
    val iso: String = "",
    val calc: CalcData = CalcData(),
    val customer: CustomerInfo = CustomerInfo(),
    val edits: List<EditTrace> = emptyList()
)
