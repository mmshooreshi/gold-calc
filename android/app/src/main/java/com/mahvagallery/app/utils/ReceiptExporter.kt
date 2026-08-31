package com.mahvagallery.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.mahvagallery.app.model.CalcData
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
        date: String,
        time: String,
        title: String
    ) {
        try {
            val pdfDocument = PdfDocument()
            val pageWidth = 250 // ~88mm
            val pageHeight = 420
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Background
            val bgPaint = Paint().apply { color = Color.parseColor("#FFFDF8") }
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

            val titlePaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 16f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }

            val textPaint = Paint().apply {
                color = Color.parseColor("#333333")
                textSize = 10f
                textAlign = Paint.Align.RIGHT
            }

            val leftTextPaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 10.5f
                isFakeBoldText = true
                textAlign = Paint.Align.LEFT
            }

            val linePaint = Paint().apply {
                color = Color.parseColor("#CCCCCC")
                strokeWidth = 1f
            }

            var y = 28f
            canvas.drawText("گالری طلا مهوا", pageWidth / 2f, y, titlePaint)
            y += 16f

            titlePaint.textSize = 10f
            titlePaint.color = Color.parseColor("#555555")
            canvas.drawText(title, pageWidth / 2f, y, titlePaint)
            y += 18f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 16f

            // Date & Time
            canvas.drawText("تاریخ: ${NumberFormatters.toPersianDigits(date)}", pageWidth - 15f, y, textPaint)
            canvas.drawText("زمان: ${NumberFormatters.toPersianDigits(time)}", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 18f

            // Weight & Gold Price
            canvas.drawText("وزن طلا:", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatWeight(calcData.b)} گرم", 15f, y, leftTextPaint)
            y += 16f

            canvas.drawText("فی طلا (خام):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.a)} ت", 15f, y, leftTextPaint)
            y += 16f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 16f

            // Ojrat, Profit, Tax
            canvas.drawText("اجرت (${NumberFormatters.formatPercentage(calcData.d)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.e)} ت", 15f, y, leftTextPaint)
            y += 16f

            canvas.drawText("سود (${NumberFormatters.formatPercentage(calcData.f)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.g)} ت", 15f, y, leftTextPaint)
            y += 16f

            canvas.drawText("مالیات (${NumberFormatters.formatPercentage(calcData.h)}٪):", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.i)} ت", 15f, y, leftTextPaint)
            y += 18f

            val totalCosts = calcData.e + calcData.g + calcData.i
            canvas.drawText("مجموع هزینه‌ها:", pageWidth - 15f, y, textPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(totalCosts)} ت", 15f, y, leftTextPaint)
            y += 18f

            canvas.drawLine(15f, y, pageWidth - 15f, y, linePaint)
            y += 22f

            // Total
            val totalLabelPaint = Paint().apply {
                color = Color.parseColor("#111111")
                textSize = 13f
                isFakeBoldText = true
                textAlign = Paint.Align.RIGHT
            }
            val totalValPaint = Paint().apply {
                color = Color.parseColor("#172051")
                textSize = 14f
                isFakeBoldText = true
                textAlign = Paint.Align.LEFT
            }
            canvas.drawText("مبلغ کل:", pageWidth - 15f, y, totalLabelPaint)
            canvas.drawText("${NumberFormatters.formatCurrency(calcData.k)} تومان", 15f, y, totalValPaint)
            y += 24f

            // Barcode pattern
            val barPaint = Paint().apply { color = Color.parseColor("#222222") }
            val barWidths = floatArrayOf(2f, 4f, 1f, 3f, 2f, 5f, 1f, 2f, 4f, 1f, 3f, 2f, 4f, 1f, 3f, 5f, 2f, 1f, 4f, 2f, 3f, 1f, 2f, 4f)
            var curX = 30f
            for (w in barWidths) {
                canvas.drawRect(curX, y, curX + w, y + 20f, barPaint)
                curX += w + 3f
            }
            y += 30f

            titlePaint.textSize = 8.5f
            titlePaint.color = Color.parseColor("#777777")
            canvas.drawText("SYS. MAHVA / ${NumberFormatters.toPersianDigits((10000..99999).random().toString())}", pageWidth / 2f, y, titlePaint)

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

    fun shareReceiptAsText(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "اشتراک متن فاکتور"))
    }
}
