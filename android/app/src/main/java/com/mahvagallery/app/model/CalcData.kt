package com.mahvagallery.app.model

data class CalcData(
    val a: Double = 0.0,          // Raw gold price per gram (قیمت طلا)
    val b: Double = 0.0,          // Weight in grams (وزن)
    val c: Double = 0.0,          // Raw price (قیمت خام = A * B)
    val d: Double = 0.0,          // Ojrat percentage (درصد اجرت)
    val e: Double = 0.0,          // Ojrat amount (مبلغ اجرت = D * C / 100)
    val f: Double = 0.0,          // Profit percentage (درصد سود)
    val g: Double = 0.0,          // Profit amount (مبلغ سود = F * (C + E) / 100)
    val h: Double = 0.0,          // Tax percentage (درصد مالیات)
    val i: Double = 0.0,          // Tax amount (مبلغ مالیات = (E + G) * H / 100)
    val k: Double = 0.0,          // Total price (قیمت کل = C + E + G + I)
    val j: Double = 0.0,          // Final effective percentage (درصد نهایی = ((K/C) - 1) * 100)
    val totalCosts: Double = 0.0  // Total costs (مجموع هزینه‌ها = E + G + I)
)
