package com.mahvagallery.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.PersianCalendarHelper
import java.util.Calendar
import java.util.TimeZone

@Composable
fun PersianDatePickerDialog(
    initialStartDate: String = "",
    initialEndDate: String = "",
    onConfirm: (startDate: String, endDate: String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = AppTheme.colors
    val today = remember { PersianCalendarHelper.getTodayShamsi() }

    var currentYear by remember { mutableIntStateOf(today.year) }
    var currentMonth by remember { mutableIntStateOf(today.month) }

    var selectedStart by remember { mutableStateOf(initialStartDate) }
    var selectedEnd by remember { mutableStateOf(initialEndDate) }

    val daysInMonth = PersianCalendarHelper.getDaysInShamsiMonth(currentYear, currentMonth)

    // Calculate weekday of 1st day of month (Saturday = 0, Friday = 6)
    val firstDayOffset = remember(currentYear, currentMonth) {
        val gDate = PersianCalendarHelper.shamsiToGregorian(currentYear, currentMonth, 1)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply {
            set(gDate.year, gDate.month - 1, gDate.day)
        }
        val dow = cal.get(Calendar.DAY_OF_WEEK) // 1=Sun, 7=Sat
        // Map to Persian week: Saturday=0, Sunday=1, ..., Friday=6
        when (dow) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }
    }

    val weekDays = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.DateRange, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "انتخاب بازه زمانی (تقویم شمسی)",
                            fontSize = scaledSp(14f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(18.dp))
                    }
                }

                // Quick Presets Row (Today, Yesterday, This Week, This Month, etc.)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val presets = listOf(
                        "امروز" to {
                            val t = today.formatPersian()
                            selectedStart = t
                            selectedEnd = t
                        },
                        "دیروز" to {
                            val yCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -1) }
                            val s = PersianCalendarHelper.gregorianToShamsi(yCal.get(Calendar.YEAR), yCal.get(Calendar.MONTH) + 1, yCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                            selectedStart = s
                            selectedEnd = s
                        },
                        "هفته جاری" to {
                            val wCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -7) }
                            val s = PersianCalendarHelper.gregorianToShamsi(wCal.get(Calendar.YEAR), wCal.get(Calendar.MONTH) + 1, wCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                            selectedStart = s
                            selectedEnd = today.formatPersian()
                        },
                        "ماه جاری" to {
                            val s = PersianCalendarHelper.ShamsiDate(today.year, today.month, 1).formatPersian()
                            selectedStart = s
                            selectedEnd = today.formatPersian()
                        }
                    )

                    presets.forEach { (label, action) ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { action() }
                                .border(1.dp, colors.border, RoundedCornerShape(8.dp)),
                            color = colors.inputBgDisabled
                        ) {
                            Text(
                                text = label,
                                fontSize = scaledSp(10.5f),
                                fontFamily = VazirmatnFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary,
                                modifier = Modifier.padding(vertical = 5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                HorizontalDivider(color = colors.divider)

                // Month / Year Navigation Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentMonth == 12) {
                                currentMonth = 1
                                currentYear += 1
                            } else {
                                currentMonth += 1
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Filled.ChevronRight, contentDescription = "Next Month", tint = colors.primary)
                    }

                    Text(
                        text = "${PersianCalendarHelper.SHAMSI_MONTHS[currentMonth - 1]} ${NumberFormatters.toPersianDigits(currentYear.toString())}",
                        fontSize = scaledSp(14.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    IconButton(
                        onClick = {
                            if (currentMonth == 1) {
                                currentMonth = 12
                                currentYear -= 1
                            } else {
                                currentMonth -= 1
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Filled.ChevronLeft, contentDescription = "Prev Month", tint = colors.primary)
                    }
                }

                // Weekday Headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weekDays.forEach { wDay ->
                        Text(
                            text = wDay,
                            fontSize = scaledSp(11.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (wDay == "ج") colors.danger else colors.textMuted,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Days Grid (7 columns)
                val totalCells = firstDayOffset + daysInMonth
                val totalRows = (totalCells + 6) / 7

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    for (r in 0 until totalRows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            for (c in 0..6) {
                                val cellIndex = r * 7 + c
                                val dayNum = cellIndex - firstDayOffset + 1

                                if (dayNum in 1..daysInMonth) {
                                    val dateStr = PersianCalendarHelper.ShamsiDate(currentYear, currentMonth, dayNum).formatPersian()
                                    val isStart = selectedStart == dateStr
                                    val isEnd = selectedEnd == dateStr
                                    val isToday = today.year == currentYear && today.month == currentMonth && today.day == dayNum
                                    val isInRange = selectedStart.isNotEmpty() && selectedEnd.isNotEmpty() &&
                                            dateStr > selectedStart && dateStr < selectedEnd

                                    val bgClr by animateColorAsState(
                                        targetValue = when {
                                            isStart || isEnd -> colors.primary
                                            isInRange -> colors.primary.copy(alpha = 0.15f)
                                            else -> Color.Transparent
                                        },
                                        label = "bg"
                                    )

                                    val textClr = when {
                                        isStart || isEnd -> Color.White
                                        c == 6 -> colors.danger
                                        else -> colors.textPrimary
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1.1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bgClr)
                                            .border(
                                                if (isToday && !isStart && !isEnd) 1.dp else 0.dp,
                                                if (isToday) colors.primary else Color.Transparent,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                if (selectedStart.isEmpty() || (selectedStart.isNotEmpty() && selectedEnd.isNotEmpty())) {
                                                    selectedStart = dateStr
                                                    selectedEnd = ""
                                                } else {
                                                    if (dateStr < selectedStart) {
                                                        selectedEnd = selectedStart
                                                        selectedStart = dateStr
                                                    } else {
                                                        selectedEnd = dateStr
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = NumberFormatters.toPersianDigits(dayNum.toString()),
                                            fontSize = scaledSp(12f),
                                            fontFamily = VazirmatnFontFamily,
                                            fontWeight = if (isStart || isEnd || isToday) FontWeight.Bold else FontWeight.Medium,
                                            color = textClr
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.weight(1f).aspectRatio(1.1f))
                                }
                            }
                        }
                    }
                }

                // Range Display Indicator
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = colors.inputBgDisabled,
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedStart.isNotEmpty() && selectedEnd.isNotEmpty()) {
                                "از: $selectedStart   تا: $selectedEnd"
                            } else if (selectedStart.isNotEmpty()) {
                                "تاریخ: $selectedStart (انتخاب پایان...)"
                            } else {
                                "تاریخی انتخاب نشده است"
                            },
                            fontSize = scaledSp(11.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )

                        if (selectedStart.isNotEmpty()) {
                            Text(
                                text = "پاک‌سازی",
                                fontSize = scaledSp(10.5f),
                                fontFamily = VazirmatnFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = colors.danger,
                                modifier = Modifier.clickable {
                                    selectedStart = ""
                                    selectedEnd = ""
                                }
                            )
                        }
                    }
                }

                // Action buttons
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
                        Text(text = "انصراف", color = colors.textPrimary, fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = {
                            val finalStart = selectedStart
                            val finalEnd = if (selectedEnd.isEmpty()) selectedStart else selectedEnd
                            onConfirm(finalStart, finalEnd)
                        },
                        enabled = selectedStart.isNotEmpty(),
                        modifier = Modifier.weight(1.4f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = Color.White)
                    ) {
                        Text(text = "تایید بازه انتخابی", fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
