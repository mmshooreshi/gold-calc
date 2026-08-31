package com.mahvagallery.app.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.CustomerInfo
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.ReceiptExporter
import kotlinx.coroutines.delay

@Composable
fun ReceiptDialog(
    calcData: CalcData,
    customer: CustomerInfo = CustomerInfo(),
    date: String,
    time: String,
    title: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var rollProgress by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in 1..7) {
            delay(90)
            rollProgress = i
        }
    }

    val formattedTotal = NumberFormatters.formatCurrency(calcData.k, toPersian = true)
    val formattedWeight = NumberFormatters.formatWeight(calcData.b, toPersian = true)
    val formattedRaw = NumberFormatters.formatCurrency(calcData.a, toPersian = true)
    val formattedOjrat = NumberFormatters.formatCurrency(calcData.e, toPersian = true)
    val formattedProfit = NumberFormatters.formatCurrency(calcData.g, toPersian = true)
    val formattedTax = NumberFormatters.formatCurrency(calcData.i, toPersian = true)
    val formattedCosts = NumberFormatters.formatCurrency(calcData.totalCosts, toPersian = true)

    val receiptText = buildString {
        appendLine("گالری طلا مهوا")
        appendLine(title)
        appendLine("تاریخ: ${NumberFormatters.toPersianDigits(date)}  |  زمان: ${NumberFormatters.toPersianDigits(time)}")
        if (customer.isNotEmpty) {
            appendLine("--------------------------------")
            if (customer.name.isNotEmpty()) appendLine("خریدار: ${customer.name}")
            if (customer.phone.isNotEmpty()) appendLine("شماره تماس: ${customer.phone}")
            if (customer.paymentMethod.isNotEmpty()) appendLine("روش پرداخت: ${customer.paymentMethod} ${customer.bankName}")
        }
        appendLine("--------------------------------")
        appendLine("وزن طلا: $formattedWeight گرم")
        appendLine("فی طلا: $formattedRaw تومان")
        appendLine("اجرت (${NumberFormatters.toPersianDigits(calcData.d.toString())}٪): $formattedOjrat تومان")
        appendLine("سود (${NumberFormatters.toPersianDigits(calcData.f.toString())}٪): $formattedProfit تومان")
        appendLine("مالیات (${NumberFormatters.toPersianDigits(calcData.h.toString())}٪): $formattedTax تومان")
        appendLine("--------------------------------")
        appendLine("مجموع هزینه‌ها: $formattedCosts تومان")
        appendLine("مبلغ کل: $formattedTotal تومان")
        appendLine("--------------------------------")
        appendLine("از حسن انتخاب شما سپاسگزاریم")
        appendLine("SYS. MAHVA / ${NumberFormatters.toPersianDigits("83921")}")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFFFFDF8),
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Receipt Header
                AnimatedVisibility(
                    visible = rollProgress >= 1,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 2 }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "گالری طلا مهوا",
                            fontSize = scaledSp(22f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF111111),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = title,
                            fontSize = scaledSp(12.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF555555),
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )

                        // Date & Time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "تاریخ: ${NumberFormatters.toPersianDigits(date)}", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, color = Color(0xFF555555))
                            Text(text = "زمان: ${NumberFormatters.toPersianDigits(time)}", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, color = Color(0xFF555555))
                        }

                        // Customer info box
                        if (customer.isNotEmpty) {
                            Divider(color = Color(0xFFAAAAAA), thickness = 1.dp, modifier = Modifier.padding(vertical = 6.dp))
                            if (customer.name.isNotEmpty()) ReceiptRow("خریدار:", customer.name, isBold = true)
                            if (customer.phone.isNotEmpty()) ReceiptRow("شماره تماس:", NumberFormatters.toPersianDigits(customer.phone))
                            if (customer.paymentMethod.isNotEmpty()) {
                                val payDesc = if (customer.bankName.isNotEmpty()) "${customer.paymentMethod} (${customer.bankName})" else customer.paymentMethod
                                ReceiptRow("روش پرداخت:", payDesc)
                            }
                            if (customer.trackingCode.isNotEmpty()) ReceiptRow("کد پیگیری:", NumberFormatters.toPersianDigits(customer.trackingCode))
                        }

                        Divider(color = Color(0xFFAAAAAA), thickness = 1.dp, modifier = Modifier.padding(vertical = 6.dp))
                    }
                }

                // Line Items 1: Weight & Raw
                AnimatedVisibility(
                    visible = rollProgress >= 2,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 2 }
                ) {
                    Column {
                        ReceiptRow("وزن طلا:", "$formattedWeight گـرم", isBold = true)
                        ReceiptRow("فی طلا (خام):", "$formattedRaw ت")
                        Divider(color = Color(0xFFAAAAAA), thickness = 1.dp, modifier = Modifier.padding(vertical = 6.dp))
                    }
                }

                // Line Items 2: Ojrat, Profit, Tax
                AnimatedVisibility(
                    visible = rollProgress >= 3,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 2 }
                ) {
                    Column {
                        ReceiptRow("اجرت (${NumberFormatters.formatPercentage(calcData.d)}٪):", "$formattedOjrat ت")
                        ReceiptRow("سود (${NumberFormatters.formatPercentage(calcData.f)}٪):", "$formattedProfit ت")
                        ReceiptRow("مالیات (${NumberFormatters.formatPercentage(calcData.h)}٪):", "$formattedTax ت")
                        Spacer(modifier = Modifier.height(4.dp))
                        ReceiptRow("مجموع هزینه‌ها:", "$formattedCosts ت", isMuted = true)
                        Divider(color = Color(0xFF222222), thickness = 1.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                    }
                }

                // Total Price
                AnimatedVisibility(
                    visible = rollProgress >= 4,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 2 }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "مبلغ کل:", fontSize = scaledSp(15f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Black, color = Color(0xFF111111))
                        Text(text = "$formattedTotal تومان", fontSize = scaledSp(17f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Black, color = PrimaryDark)
                    }
                }

                // Simulated Barcode
                AnimatedVisibility(
                    visible = rollProgress >= 5,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { -it / 2 }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "از حسـن انتخـاب شمـا سپـاسگزاريـم",
                            fontSize = scaledSp(11.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF555555),
                            textAlign = TextAlign.Center
                        )

                        Canvas(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(0.9f)
                                .height(28.dp)
                        ) {
                            val barWidths = listOf(
                                3f, 1.5f, 4f, 2f, 1f, 3f, 5f, 1.5f, 2f, 4f, 1f, 3.5f,
                                2f, 4f, 1f, 3f, 5f, 2f, 1.5f, 4f, 2f, 3f, 1.5f, 2f, 4f, 3f, 1.5f, 2f, 5f, 1f, 3f, 2f, 4f
                            )
                            val totalSum = barWidths.sum() + (barWidths.size * 2f)
                            val scaleFactor = size.width / totalSum
                            var currentX = 0f

                            for ((idx, w) in barWidths.withIndex()) {
                                val barW = w * scaleFactor
                                val gapW = 2f * scaleFactor
                                if (idx % 2 == 0) {
                                    drawRect(
                                        color = Color(0xFF1A1A1A),
                                        topLeft = Offset(currentX, 0f),
                                        size = Size(barW, size.height)
                                    )
                                }
                                currentX += barW + gapW
                            }
                        }

                        Text(
                            text = "SYS. MAHVA / ${NumberFormatters.toPersianDigits("83921")}",
                            fontSize = scaledSp(9.5f),
                            fontFamily = VazirmatnFontFamily,
                            color = Color(0xFF888888),
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )
                    }
                }

                // Primary Default Action: Share High-Res Image
                AnimatedVisibility(
                    visible = rollProgress >= 6,
                    enter = fadeIn(tween(250))
                ) {
                    Column {
                        Button(
                            onClick = {
                                val width = 600
                                val height = if (customer.isNotEmpty) 1000 else 900
                                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                                val canvas = AndroidCanvas(bitmap)
                                canvas.drawColor(android.graphics.Color.parseColor("#FFFDF8"))

                                val pTitle = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#172051")
                                    textSize = 36f
                                    isFakeBoldText = true
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                                val pSub = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#555555")
                                    textSize = 22f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                                val pTextRight = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#333333")
                                    textSize = 24f
                                    textAlign = android.graphics.Paint.Align.RIGHT
                                }
                                val pTextLeft = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#172051")
                                    textSize = 25f
                                    isFakeBoldText = true
                                    textAlign = android.graphics.Paint.Align.LEFT
                                }
                                val pLine = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#CCCCCC")
                                    strokeWidth = 2f
                                }

                                var y = 65f
                                canvas.drawText("گالری طلا مهوا", width / 2f, y, pTitle)
                                y += 38f
                                canvas.drawText(title, width / 2f, y, pSub)
                                y += 35f
                                canvas.drawLine(30f, y, width - 30f, y, pLine)
                                y += 38f

                                canvas.drawText("تاریخ: ${NumberFormatters.toPersianDigits(date)}", width - 30f, y, pTextRight)
                                canvas.drawText("زمان: ${NumberFormatters.toPersianDigits(time)}", 30f, y, pTextLeft)
                                y += 35f

                                if (customer.isNotEmpty) {
                                    canvas.drawLine(30f, y, width - 30f, y, pLine)
                                    y += 38f
                                    if (customer.name.isNotEmpty()) {
                                        canvas.drawText("خریدار:", width - 30f, y, pTextRight)
                                        canvas.drawText(customer.name, 30f, y, pTextLeft)
                                        y += 38f
                                    }
                                    if (customer.phone.isNotEmpty()) {
                                        canvas.drawText("شماره تماس:", width - 30f, y, pTextRight)
                                        canvas.drawText(customer.phone, 30f, y, pTextLeft)
                                        y += 38f
                                    }
                                }

                                canvas.drawLine(30f, y, width - 30f, y, pLine)
                                y += 40f

                                canvas.drawText("وزن طلا:", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedWeight گرم", 30f, y, pTextLeft)
                                y += 40f
                                canvas.drawText("فی طلا (خام):", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedRaw ت", 30f, y, pTextLeft)
                                y += 35f
                                canvas.drawLine(30f, y, width - 30f, y, pLine)
                                y += 40f

                                canvas.drawText("اجرت (${NumberFormatters.formatPercentage(calcData.d)}٪):", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedOjrat ت", 30f, y, pTextLeft)
                                y += 40f
                                canvas.drawText("سود (${NumberFormatters.formatPercentage(calcData.f)}٪):", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedProfit ت", 30f, y, pTextLeft)
                                y += 40f
                                canvas.drawText("مالیات (${NumberFormatters.formatPercentage(calcData.h)}٪):", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedTax ت", 30f, y, pTextLeft)
                                y += 40f

                                canvas.drawText("مجموع هزینه‌ها:", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedCosts ت", 30f, y, pTextLeft)
                                y += 35f
                                canvas.drawLine(30f, y, width - 30f, y, pLine)
                                y += 50f

                                pTextRight.textSize = 28f
                                pTextRight.isFakeBoldText = true
                                pTextLeft.textSize = 32f
                                pTextLeft.color = android.graphics.Color.parseColor("#172051")
                                canvas.drawText("مبلغ کل:", width - 30f, y, pTextRight)
                                canvas.drawText("$formattedTotal تومان", 30f, y, pTextLeft)
                                y += 55f

                                val pBarcode = android.graphics.Paint().apply {
                                    color = android.graphics.Color.parseColor("#111111")
                                    style = android.graphics.Paint.Style.FILL
                                }
                                val bWidths = floatArrayOf(4f, 8f, 2f, 6f, 4f, 10f, 2f, 4f, 8f, 2f, 6f, 4f, 8f, 2f, 6f, 10f, 4f, 2f, 8f, 4f, 6f, 2f, 4f, 8f)
                                var bX = 60f
                                for (w in bWidths) {
                                    canvas.drawRect(bX, y, bX + w, y + 40f, pBarcode)
                                    bX += w + 6f
                                }
                                y += 65f

                                pSub.textSize = 18f
                                canvas.drawText("از حسن انتخاب شما سپاسگزاریم", width / 2f, y, pSub)

                                ReceiptExporter.shareReceiptAsImage(context, bitmap)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = Color.White)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "اشتراک تصویر فاکتور (تلگرام و...)", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Secondary Action Row: Share PDF, Share Text, Close
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(0.8f).height(40.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "بستن", color = TextDark, fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = {
                                    ReceiptExporter.shareReceiptAsText(context, receiptText)
                                },
                                modifier = Modifier.weight(0.9f).height(40.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp), tint = TextDark)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "متن", color = TextDark, fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold)
                            }

                            Button(
                                onClick = {
                                    ReceiptExporter.shareReceiptAsPdf(context, calcData, customer, date, time, title)
                                },
                                modifier = Modifier.weight(1.1f).height(40.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3675), contentColor = Color.White)
                            ) {
                                Icon(imageVector = Icons.Filled.PictureAsPdf, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "فایل PDF", fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isMuted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = scaledSp(12.5f),
            fontFamily = VazirmatnFontFamily,
            color = if (isMuted) Color(0xFF666666) else Color(0xFF222222),
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = scaledSp(13f),
            fontFamily = VazirmatnFontFamily,
            color = if (isMuted) Color(0xFF666666) else Color(0xFF111111),
            fontWeight = if (isBold) FontWeight.Black else FontWeight.Bold
        )
    }
}
