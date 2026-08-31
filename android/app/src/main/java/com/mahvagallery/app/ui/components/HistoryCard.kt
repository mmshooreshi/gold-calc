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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.model.HistoryItem
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun HistoryCard(
    item: HistoryItem,
    onEdit: (HistoryItem) -> Unit,
    onDelete: (Long) -> Unit,
    onShowReceipt: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isEdited = item.edits.isNotEmpty()

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
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
        color = White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Card Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
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
                                if (item.type == "sale") Color(0xFF10B981).copy(alpha = 0.15f)
                                else Color(0xFF64748B).copy(alpha = 0.12f)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (item.type == "sale") "فروش" else "محاسبه",
                            color = if (item.type == "sale") Color(0xFF10B981) else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isEdited) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B))
                        )
                    }

                    Text(
                        text = "$totalFormatted ت",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextDark
                    )
                }

                // Time, Receipt and Chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF172051).copy(alpha = 0.08f))
                            .clickable { onShowReceipt(item) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Receipt,
                            contentDescription = "Receipt",
                            modifier = Modifier.size(16.dp),
                            tint = PrimaryDark
                        )
                    }

                    Text(
                        text = NumberFormatters.toPersianDigits(item.time),
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextMuted,
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
                        .background(DisabledBg)
                        .padding(14.dp)
                ) {
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
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "درصد نهایی:", fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Text(text = "$finalPercentFormatted٪", fontSize = 13.sp, color = PrimaryDark, fontWeight = FontWeight.Black)
                    }

                    // Edit trace history
                    if (isEdited) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderColor)
                        Text(
                            text = "ویرایش‌های قبلی:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        item.edits.forEach { trace ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${trace.date} ${NumberFormatters.toPersianDigits(trace.time)}",
                                    fontSize = 10.5.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = "${NumberFormatters.formatCurrency(trace.calc.k, toPersian = true)} ت",
                                    fontSize = 10.5.sp,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Action buttons (Edit & Delete)
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = BorderColor)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onEdit(item) },
                            color = Color(0xFFF59E0B).copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFFD97706))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "ویرایش", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onDelete(item.id) },
                            color = Color(0xFFEF4444).copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFFDC2626))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "حذف", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = label, fontSize = 10.5.sp, color = TextMuted, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 12.5.sp, color = TextDark, fontWeight = FontWeight.Bold)
    }
}
