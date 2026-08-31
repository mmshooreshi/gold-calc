package com.mahvagallery.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.CustomerInfo
import com.mahvagallery.app.model.HistoryItem
import java.io.File
import java.io.FileOutputStream

object ReceiptExporter {

    fun shareReceiptAsImage(context: Context, bitmap: Bitmap) {
        try {
            val imagesFolder = File(context.cacheDir, "images").apply { mkdirs() }
            val file = File(imagesFolder, "receipt_${System.currentTimeMillis()}.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "فاکتور طلا مهوا گالری")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "اشتراک تصویر فاکتور (تلگرام، واتساپ و...)"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareReceiptAsPdf(
        context: Context,
        calcData: CalcData,
        customer: CustomerInfo = CustomerInfo(),
        date: String,
        time: String,
        title: String
    ) {
        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 280
            val pageHeight = if (customer.isNotEmpty) 540 else 480
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Background
            val bgPaint = Paint().apply {
                color = Color.parseColor("#FFFDF8")
                style = Paint.Style.FILL
            }
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

            val titlePaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 18f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }

            val textPaint = Paint().apply {
                color = Color.parseColor("#333333")
                textSize = 11f
                textAlign = Paint.Align.RIGHT
            }

            val leftTextPaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 11.5f
                isFakeBoldText = true
                textAlign = Paint.Align.LEFT
            }

            val linePaint = Paint().apply {
                color = Color.parseColor("#CCCCCC")
                strokeWidth = 1f
                style = Paint.Style.STROKE
            }

            var y = 32f
            canvas.drawText("گالری طلا مهوا", pageWidth / 2f, y, titlePaint)
            y += 20f

            titlePaint.textSize = 11f
            titlePaint.color = Color.parseColor("#555555")
            canvas.drawText(title, pageWidth / 2f, y, titlePaint)
            y += 20f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 18f

            // Date & Time
            canvas.drawText("تاریخ: ${NumberFormatters.toPersianDigits(date)}", pageWidth - 15f, y, textPaint)
            canvas.drawText("زمان: ${NumberFormatters.toPersianDigits(time)}", 15f, y, leftTextPaint)
            y += 20f

            // Customer details if any
            if (customer.isNotEmpty) {
                canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
                y += 18f

                if (customer.name.isNotEmpty()) {
                    canvas.drawText("خریدار:", pageWidth - 15f, y, textPaint)
                    canvas.drawText(customer.name, 15f, y, leftTextPaint)
                    y += 18f
                }
                if (customer.phone.isNotEmpty()) {
                    canvas.drawText("شماره تماس:", pageWidth - 15f, y, textPaint)
                    canvas.drawText(NumberFormatters.toPersianDigits(customer.phone), 15f, y, leftTextPaint)
                    y += 18f
                }
                if (customer.paymentMethod.isNotEmpty()) {
                    val payText = if (customer.bankName.isNotEmpty()) "${customer.paymentMethod} (${customer.bankName})" else customer.paymentMethod
                    canvas.drawText("روش پرداخت:", pageWidth - 15f, y, textPaint)
                    canvas.drawText(payText, 15f, y, leftTextPaint)
                    y += 18f
                }
                if (customer.trackingCode.isNotEmpty()) {
                    canvas.drawText("کد پیگیری:", pageWidth - 15f, y, textPaint)
                    canvas.drawText(NumberFormatters.toPersianDigits(customer.trackingCode), 15f, y, leftTextPaint)
                    y += 18f
                }
            }

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 20f

            // Weight & Gold Price
            canvas.drawText("وزن طلا:", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatWeight(calcData.b, toPersian = true)} گرم", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawText("فی طلا (خام):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.a, toPersian = true)} ت", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 18f

            // Ojrat, Profit, Tax
            canvas.drawText("اجرت (${NumberFormatters.formatPercentage(calcData.d)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.e, toPersian = true)} ت", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawText("سود (${NumberFormatters.formatPercentage(calcData.f)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.g, toPersian = true)} ت", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawText("مالیات (${NumberFormatters.formatPercentage(calcData.h)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.i, toPersian = true)} ت", 15f, y, leftTextPaint)
            y += 20f

            val totalCosts = calcData.e + calcData.g + calcData.i
            canvas.drawText("مجموع هزینه‌ها:", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(totalCosts, toPersian = true)} ت", 15f, y, leftTextPaint)
            y += 20f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 24f

            // Total
            val totalLabelPaint = Paint().apply {
                color = Color.parseColor("#111111")
                textSize = 14f
                isFakeBoldText = true
                textAlign = Paint.Align.RIGHT
            }
            val totalValPaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 15f
                isFakeBoldText = true
                textAlign = Paint.Align.LEFT
            }
            canvas.drawText("مبلغ کل:", pageWidth - 15f, y, totalLabelPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.k, toPersian = true)} تومان", 15f, y, totalValPaint)
            y += 28f

            // Barcode pattern with explicit Solid Fill
            val barPaint = Paint().apply {
                color = Color.BLACK
                style = Paint.Style.FILL
            }
            val barWidths = floatArrayOf(
                3f, 1.5f, 4f, 2f, 1f, 3f, 5f, 1.5f, 2f, 4f, 1f, 3.5f,
                2f, 4f, 1f, 3f, 5f, 2f, 1.5f, 4f, 2f, 3f, 1.5f, 2f, 4f, 3f, 1.5f, 2f, 5f, 1f, 3f, 2f, 4f
            )
            var curX = 25f
            val barHeight = 26f
            for (w in barWidths) {
                canvas.drawRect(curX, y, curX + w, y + barHeight, barPaint)
                curX += w + 2.5f
            }
            y += barHeight + 14f

            titlePaint.textSize = 9.5f
            titlePaint.color = Color.parseColor("#777777")
            canvas.drawText("SYS. MAHVA / ${NumberFormatters.toPersianDigits("83921")}", pageWidth / 2f, y, titlePaint)

            pdfDocument.finishPage(page)

            val docsFolder = File(context.cacheDir, "docs").apply { mkdirs() }
            val file = File(docsFolder, "Mahva_Receipt_${System.currentTimeMillis()}.pdf")
            val stream = FileOutputStream(file)
            pdfDocument.writeTo(stream)
            pdfDocument.close()
            stream.flush()
            stream.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "فاکتور طلا مهوا")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "ارسال PDF فاکتور (تلگرام، واتساپ و...)"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exportStatsPdf(
        context: Context,
        sales: List<HistoryItem>,
        totalAmount: Double,
        count: Int,
        avgAmount: Double,
        maxAmount: Double,
        presetTitle: String
    ) {
        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 400
            val pageHeight = (280 + sales.size * 28).coerceAtLeast(400)
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val bgPaint = Paint().apply { color = Color.parseColor("#F8FAFC"); style = Paint.Style.FILL }
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

            val headerPaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 18f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }
            var y = 35f
            canvas.drawText("گزارش فروش و عملکرد مهوا گالری", pageWidth / 2f, y, headerPaint)
            y += 22f

            headerPaint.textSize = 12f
            headerPaint.color = Color.parseColor("#64748B")
            canvas.drawText("بازه زمانی: $presetTitle", pageWidth / 2f, y, headerPaint)
            y += 25f

            val cardPaint = Paint().apply { color = Color.WHITE; style = Paint.Style.FILL }
            canvas.drawRoundRect(20f, y, pageWidth - 20f, y + 65f, 12f, 12f, cardPaint)

            val textPaint = Paint().apply { color = Color.parseColor("#1E293B"); textSize = 11.5f; textAlign = Paint.Align.RIGHT }
            val boldPaint = Paint().apply { color = Color.parseColor("#172051"); textSize = 12.5f; isFakeBoldText = true; textAlign = Paint.Align.LEFT }

            canvas.drawText("مجموع فروش:", pageWidth - 35f, y + 25f, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(totalAmount, toPersian = true)} تومان", 35f, y + 25f, boldPaint)

            canvas.drawText("تعداد تراکنش‌ها:", pageWidth - 35f, y + 48f, textPaint)
            canvas.drawText("${NumberFormatters.toPersianDigits(count.toString())} عدد", 35f, y + 48f, boldPaint)
            y += 85f

            val rowPaint = Paint().apply { color = Color.parseColor("#334155"); textSize = 11f; textAlign = Paint.Align.RIGHT }
            val linePaint = Paint().apply { color = Color.parseColor("#CBD5E1"); strokeWidth = 1f }

            canvas.drawText("ریز تراکنش‌های فروش:", pageWidth - 20f, y, textPaint)
            y += 18f

            sales.take(50).forEachIndexed { idx, item ->
                val line = "${idx + 1}. تاریخ: ${NumberFormatters.toPersianDigits(item.date)} | وزن: ${NumberFormatters.formatWeight(item.calc.b, toPersian = true)} گرم"
                canvas.drawText(line, pageWidth - 25f, y, rowPaint)
                canvas.drawText("${NumberFormatters.formatCurrency(item.calc.k, toPersian = true)} ت", 25f, y, boldPaint)
                y += 14f
                canvas.drawLine(25f, y, pageWidth - 25f, y, linePaint)
                y += 14f
            }

            pdfDocument.finishPage(page)

            val docsFolder = File(context.cacheDir, "docs").apply { mkdirs() }
            val file = File(docsFolder, "Mahva_SalesReport_${System.currentTimeMillis()}.pdf")
            val stream = FileOutputStream(file)
            pdfDocument.writeTo(stream)
            pdfDocument.close()
            stream.flush()
            stream.close()

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "گزارش فروش مهوا گالری")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "ارسال PDF گزارش فروش"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareReceiptAsText(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "اشتراک متن فاکتور"))
    }
}
