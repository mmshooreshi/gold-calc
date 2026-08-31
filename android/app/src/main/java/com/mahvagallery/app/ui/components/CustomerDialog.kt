package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahvagallery.app.model.CustomerInfo
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp

@Composable
fun CustomerDialog(
    initialInfo: CustomerInfo,
    onSave: (CustomerInfo) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = AppTheme.colors
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf(initialInfo.name) }
    var phone by remember { mutableStateOf(initialInfo.phone) }
    var nationalId by remember { mutableStateOf(initialInfo.nationalId) }
    var paymentMethod by remember { mutableStateOf(initialInfo.paymentMethod.ifEmpty { "کارتخوان" }) }
    var bankName by remember { mutableStateOf(initialInfo.bankName) }
    var trackingCode by remember { mutableStateOf(initialInfo.trackingCode) }
    var note by remember { mutableStateOf(initialInfo.note) }

    val paymentOptions = listOf("کارتخوان", "نقدی", "چک", "حواله پایا")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.border),
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "مشخصات مشتری و پرداخت",
                            fontSize = scaledSp(15f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onDismiss),
                        color = colors.inputBgDisabled
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = colors.textMuted)
                        }
                    }
                }

                // Field 1: Customer Name
                CustomerField(
                    label = "نام و نام خانوادگی",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "مثلاً: علی محمدی",
                    keyboardType = KeyboardType.Text
                )

                // Field 2: Phone Number
                CustomerField(
                    label = "شماره تماس",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "۰۹۱۲-------",
                    keyboardType = KeyboardType.Phone
                )

                // Field 3: National ID
                CustomerField(
                    label = "کد ملی (اختیاری)",
                    value = nationalId,
                    onValueChange = { nationalId = it },
                    placeholder = "کد ملی ۱۰ رقمی",
                    keyboardType = KeyboardType.Number
                )

                // Payment Method Selector Chips
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "روش پرداخت:",
                        fontSize = scaledSp(12f),
                        fontFamily = VazirmatnFontFamily,
                        color = colors.textMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        paymentOptions.forEach { method ->
                            val isSel = paymentMethod == method
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { paymentMethod = method }
                                    .border(
                                        1.dp,
                                        if (isSel) colors.primary else colors.border,
                                        RoundedCornerShape(10.dp)
                                    ),
                                color = if (isSel) colors.primary else colors.inputBgDisabled
                            ) {
                                Text(
                                    text = method,
                                    fontSize = scaledSp(11f),
                                    fontFamily = VazirmatnFontFamily,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else colors.textPrimary,
                                    modifier = Modifier.padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Bank & Tracking code
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomerField(
                        modifier = Modifier.weight(1f),
                        label = "نام بانک / پوز",
                        value = bankName,
                        onValueChange = { bankName = it },
                        placeholder = "مثلاً ملت / سامان",
                        keyboardType = KeyboardType.Text
                    )

                    CustomerField(
                        modifier = Modifier.weight(1f),
                        label = "شماره پیگیری",
                        value = trackingCode,
                        onValueChange = { trackingCode = it },
                        placeholder = "کد ارجاع یا چک",
                        keyboardType = KeyboardType.Number
                    )
                }

                // Note
                CustomerField(
                    label = "یادداشت / توضیحات فاکتور",
                    value = note,
                    onValueChange = { note = it },
                    placeholder = "توضیحات اختیاری...",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                    ) {
                        Text(text = "انصراف", color = colors.textDarkOrTextPrimary(), fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily)
                    }

                    Button(
                        onClick = {
                            onSave(
                                CustomerInfo(
                                    name = name,
                                    phone = phone,
                                    nationalId = nationalId,
                                    paymentMethod = paymentMethod,
                                    bankName = bankName,
                                    trackingCode = trackingCode,
                                    note = note
                                )
                            )
                        },
                        modifier = Modifier.weight(1.5f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = Color.White)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "ثبت مشخصات", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = label,
            fontSize = scaledSp(11.5f),
            fontFamily = VazirmatnFontFamily,
            color = colors.textMuted,
            fontWeight = FontWeight.Medium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.inputBg)
                .border(1.dp, colors.inputBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    fontFamily = VazirmatnFontFamily,
                    color = colors.textPrimary,
                    fontSize = scaledSp(12.5f),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    textDirection = TextDirection.Rtl
                ),
                cursorBrush = SolidColor(colors.primary),
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.textMuted.copy(alpha = 0.5f),
                            fontSize = scaledSp(11.5f),
                            fontFamily = VazirmatnFontFamily
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun com.mahvagallery.app.ui.theme.AppCustomColors.textDarkOrTextPrimary(): Color {
    return if (isDark) textPrimary else textPrimary
}
