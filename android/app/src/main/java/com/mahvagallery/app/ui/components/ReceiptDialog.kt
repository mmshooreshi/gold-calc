package com.mahvagallery.app.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun ReceiptDialog(
    calcData: CalcData,
    date: String,
    time: String,
    title: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val formattedTotal = NumberFormatters.formatCurrency(calcData.k, toPersian = true)
    val formattedWeight = NumberFormatters.formatWeight(calcData.b, toPersian = true)
    val formattedRaw = NumberFormatters.formatCurrency(calcData.a, toPersian = true)
    val formattedOjrat = NumberFormatters.formatCurrency(calcData.e, toPersian = true)
    val formattedProfit = NumberFormatters.formatCurrency(calcData.g, toPersian = true)
    val formattedTax = NumberFormatters.formatCurrency(calcData.i, toPersian = true)
    val formattedCosts = NumberFormatters.formatCurrency(calcData.totalCosts, toPersian = true)

    val receiptText = """
        گالری طلا مهوا
        $title
        تاریخ: $date  |  زمان: $time
        --------------------------------
        وزن طلا: $formattedWeight گرم
        فی طلا: $formattedRaw تومان
        اجرت (${NumberFormatters.toPersianDigits(calcData.d.toString())}٪): $formattedOjrat تومان
        سود (${NumberFormatters.toPersianDigits(calcData.f.toString())}٪): $formattedProfit تومان
        مالیات (${NumberFormatters.toPersianDigits(calcData.h.toString())}٪): $formattedTax تومان
        --------------------------------
        مجموع هزینه‌ها: $formattedCosts تومان
        مبلغ کل: $formattedTotal تومان
        --------------------------------
        از حسن انتخاب شما سپاسگزاریم
    """.trimIndent()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFFDF8),
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Receipt Header
                Text(
                    text = "گالری طلا مهوا",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF111111),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                // Date & Time
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "تاریخ: $date", fontSize = 12.sp, color = Color(0xFF555555))
                    Text(text = "زمان: $time", fontSize = 12.sp, color = Color(0xFF555555))
                }

                Divider(color = Color(0xFFAAAAAA), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // Line Items
                ReceiptRow("وزن طلا:", "$formattedWeight گـرم", isBold = true)
                ReceiptRow("فی طلا (خام):", "$formattedRaw ت")

                Divider(color = Color(0xFFAAAAAA), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                ReceiptRow("اجرت (${NumberFormatters.toPersianDigits(calcData.d.toString())}٪):", "$formattedOjrat ت")
                ReceiptRow("سود (${NumberFormatters.toPersianDigits(calcData.f.toString())}٪):", "$formattedProfit ت")
                ReceiptRow("مالیات (${NumberFormatters.toPersianDigits(calcData.h.toString())}٪):", "$formattedTax ت")

                Spacer(modifier = Modifier.height(4.dp))
                ReceiptRow("مجموع هزینه‌ها:", "$formattedCosts ت", isMuted = true)

                Divider(color = Color(0xFF222222), thickness = 1.5.dp, modifier = Modifier.padding(vertical = 10.dp))

                // Total Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "مبلغ کل:", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF111111))
                    Text(text = "$formattedTotal تومان", fontSize = 17.sp, fontWeight = FontWeight.Black, color = PrimaryDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Thank you note
                Text(
                    text = "از حسـن انتخـاب شمـا سپـاسگزاريـم",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF555555),
                    textAlign = TextAlign.Center
                )

                // Simulated Barcode
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(0.85f)
                        .height(28.dp)
                        .background(Color(0xFF111111).copy(alpha = 0.85f))
                )

                Text(
                    text = "SYS. MAHVA / ${NumberFormatters.toPersianDigits((10000..99999).random().toString())}",
                    fontSize = 10.sp,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "بستن", color = TextDark, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, receiptText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "اشتراک‌گذاری فاکتور"))
                        },
                        modifier = Modifier.weight(1.3f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "اشتراک فاکتور", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
            fontSize = 12.5.sp,
            color = if (isMuted) Color(0xFF666666) else Color(0xFF222222),
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = if (isMuted) Color(0xFF666666) else Color(0xFF111111),
            fontWeight = if (isBold) FontWeight.Black else FontWeight.Bold
        )
    }
}
