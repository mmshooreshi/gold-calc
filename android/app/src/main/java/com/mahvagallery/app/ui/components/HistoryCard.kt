package com.mahvagallery.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.model.CalcData
import com.mahvagallery.app.model.EditTrace
import com.mahvagallery.app.model.HistoryItem
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun HistoryCard(
    item: HistoryItem,
    onEdit: (HistoryItem) -> Unit,
    onDelete: (HistoryItem) -> Unit,
    onShowReceipt: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isHistoryDiffExpanded by remember { mutableStateOf(false) }
    val isEdited = item.edits.isNotEmpty()
    val isDraft = item.type == "draft"
    val colors = AppTheme.colors

    val totalFormatted = NumberFormatters.formatCurrency(item.calc.k, toPersian = true)
    val weightFormatted = NumberFormatters.formatWeight(item.calc.b, toPersian = true)
    val rawGoldFormatted = NumberFormatters.formatCurrency(item.calc.a, toPersian = true)
    val ojratFormatted = NumberFormatters.formatCurrency(item.calc.e, toPersian = true)
    val profitFormatted = NumberFormatters.formatCurrency(item.calc.g, toPersian = true)
    val taxFormatted = NumberFormatters.formatCurrency(item.calc.i, toPersian = true)
    val totalCostsFormatted = NumberFormatters.formatCurrency(item.calc.totalCosts, toPersian = true)
    val finalPercentFormatted = NumberFormatters.formatPercentage(item.calc.j, toPersian = true)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isDraft) colors.warning.copy(alpha = 0.5f) else colors.border,
                RoundedCornerShape(14.dp)
            ),
        color = colors.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Card Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                if (isDraft) colors.warning.copy(alpha = 0.18f)
                                else colors.success.copy(alpha = 0.15f)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isDraft) "پیش‌نویس" else "فروش",
                            color = if (isDraft) colors.warning else colors.success,
                            fontSize = scaledSp(11f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isEdited) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(colors.warning)
                        )
                    }

                    Text(
                        text = "$totalFormatted ت",
                        fontSize = scaledSp(14.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Black,
                        color = colors.textPrimary
                    )

                    if (item.customer.isNotEmpty && item.customer.name.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.customer.name,
                                fontSize = scaledSp(10.5f),
                                fontFamily = VazirmatnFontFamily,
                                color = colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Time, Receipt and Chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.primary.copy(alpha = 0.12f))
                            .clickable { onShowReceipt(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Receipt,
                            contentDescription = "Receipt",
                            modifier = Modifier.size(17.dp),
                            tint = colors.primary
                        )
                    }

                    Text(
                        text = NumberFormatters.toPersianDigits(item.time),
                        fontSize = scaledSp(12f),
                        fontFamily = VazirmatnFontFamily,
                        color = colors.textMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colors.textMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expandable Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.inputBgDisabled)
                        .padding(12.dp)
                ) {
                    // Customer Details Card if available
                    if (item.customer.isNotEmpty) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = colors.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Person, contentDescription = null, tint = colors.primary, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "مشخصات خریدار و پرداخت", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.primary)
                                }
                                if (item.customer.name.isNotEmpty()) DetailItem("نام مشتری:", item.customer.name)
                                if (item.customer.phone.isNotEmpty()) DetailItem("شماره تماس:", NumberFormatters.toPersianDigits(item.customer.phone))
                                if (item.customer.paymentMethod.isNotEmpty()) DetailItem("پرداخت:", "${item.customer.paymentMethod} ${item.customer.bankName}")
                                if (item.customer.trackingCode.isNotEmpty()) DetailItem("کد رهگیری:", NumberFormatters.toPersianDigits(item.customer.trackingCode))
                            }
                        }
                    }

                    // Breakdown Grid
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            DetailItem("وزن:", "$weightFormatted گـرم")
                            DetailItem("اجرت:", "$ojratFormatted ت")
                            DetailItem("مالیات:", "$taxFormatted ت")
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            DetailItem("طلا (خام):", "$rawGoldFormatted ت")
                            DetailItem("سود:", "$profitFormatted ت")
                            DetailItem("مجموع هزینه‌ها:", "$totalCostsFormatted ت")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "درصد نهایی:", fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, color = colors.textMuted, fontWeight = FontWeight.Bold)
                        Text(text = "$finalPercentFormatted٪", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, color = colors.primary, fontWeight = FontWeight.Black)
                    }

                    // Edit trace history with FULL DETAILED DIFF
                    if (isEdited) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = colors.divider)
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { isHistoryDiffExpanded = !isHistoryDiffExpanded }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.History, contentDescription = null, tint = colors.warning, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "تاریخچه ویرایش‌ها و تغییرات مقادیر (${NumberFormatters.toPersianDigits(item.edits.size.toString())}):",
                                    fontSize = scaledSp(11.5f),
                                    fontFamily = VazirmatnFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.warning
                                )
                            }
                            Icon(
                                imageVector = if (isHistoryDiffExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = colors.warning,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = isHistoryDiffExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                item.edits.forEachIndexed { index, trace ->
                                    val previousCalc = if (index + 1 < item.edits.size) item.edits[index + 1].calc else item.calc
                                    
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        color = colors.surface,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "ویرایش در: ${trace.date} ${NumberFormatters.toPersianDigits(trace.time)}",
                                                    fontSize = scaledSp(10.5f),
                                                    fontFamily = VazirmatnFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colors.primary
                                                )
                                                Text(
                                                    text = "مبلغ: ${NumberFormatters.formatCurrency(trace.calc.k, toPersian = true)} ت",
                                                    fontSize = scaledSp(10.5f),
                                                    fontFamily = VazirmatnFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colors.textPrimary
                                                )
                                            }

                                            HorizontalDivider(color = colors.divider, modifier = Modifier.padding(vertical = 2.dp))

                                            // Field-by-field differences
                                            if (trace.calc.b != item.calc.b) {
                                                DiffRow("وزن طلا:", "${NumberFormatters.formatWeight(trace.calc.b, toPersian = true)} گـرم ➔ ${NumberFormatters.formatWeight(item.calc.b, toPersian = true)} گـرم")
                                            }
                                            if (trace.calc.a != item.calc.a) {
                                                DiffRow("قیمت طلا:", "${NumberFormatters.formatCurrency(trace.calc.a, toPersian = true)} ت ➔ ${NumberFormatters.formatCurrency(item.calc.a, toPersian = true)} ت")
                                            }
                                            if (trace.calc.d != item.calc.d) {
                                                DiffRow("درصد اجرت:", "${NumberFormatters.formatPercentage(trace.calc.d)}٪ ➔ ${NumberFormatters.formatPercentage(item.calc.d)}٪")
                                            }
                                            if (trace.calc.f != item.calc.f) {
                                                DiffRow("درصد سود:", "${NumberFormatters.formatPercentage(trace.calc.f)}٪ ➔ ${NumberFormatters.formatPercentage(item.calc.f)}٪")
                                            }
                                            if (trace.calc.h != item.calc.h) {
                                                DiffRow("درصد مالیات:", "${NumberFormatters.formatPercentage(trace.calc.h)}٪ ➔ ${NumberFormatters.formatPercentage(item.calc.h)}٪")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Action buttons (Edit & Delete)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = colors.divider)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onEdit(item) },
                            color = colors.warning.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 7.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(15.dp), tint = colors.warning)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isDraft) "تکمیل و ثبت" else "ویرایش", fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.warning)
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onDelete(item) },
                            color = colors.danger.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 7.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(15.dp), tint = colors.danger)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "حذف", fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.danger)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiffRow(label: String, diffText: String) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = scaledSp(10f), fontFamily = VazirmatnFontFamily, color = colors.textMuted)
        Text(text = diffText, fontSize = scaledSp(10.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.warning)
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, color = colors.textMuted, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, color = colors.textPrimary, fontWeight = FontWeight.Bold)
    }
}
