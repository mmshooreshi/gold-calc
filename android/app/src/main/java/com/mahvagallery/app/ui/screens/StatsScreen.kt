package com.mahvagallery.app.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.ui.components.PersianDatePickerDialog
import com.mahvagallery.app.ui.components.SimpleBarChart
import com.mahvagallery.app.ui.components.SimpleLineChart
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.PersianCalendarHelper
import com.mahvagallery.app.utils.ReceiptExporter
import com.mahvagallery.app.viewmodel.MainViewModel
import com.mahvagallery.app.viewmodel.StatsDatePreset
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val history by viewModel.repository.history.collectAsState()
    val activePreset by viewModel.statsDatePreset.collectAsState()
    val colors = AppTheme.colors
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val presetsScrollState = rememberScrollState()

    var isCustomRangeActive by remember { mutableStateOf(false) }
    var customStartDate by remember { mutableStateOf("") }
    var customEndDate by remember { mutableStateOf("") }
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Filter sales by preset or calendar custom date range
    val sales = remember(history, activePreset, isCustomRangeActive, customStartDate, customEndDate) {
        val allSales = history.filter { it.type == "sale" }
        if (isCustomRangeActive && (customStartDate.isNotEmpty() || customEndDate.isNotEmpty())) {
            allSales.filter { item ->
                val date = item.date
                val afterStart = if (customStartDate.isNotEmpty()) date >= customStartDate else true
                val beforeEnd = if (customEndDate.isNotEmpty()) date <= customEndDate else true
                afterStart && beforeEnd
            }
        } else {
            when (activePreset) {
                StatsDatePreset.ALL -> allSales
                StatsDatePreset.TODAY -> {
                    val today = PersianCalendarHelper.getTodayShamsi().formatPersian()
                    allSales.filter { it.date == today }
                }
                StatsDatePreset.YESTERDAY -> {
                    val yCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -1) }
                    val yShamsi = PersianCalendarHelper.gregorianToShamsi(yCal.get(Calendar.YEAR), yCal.get(Calendar.MONTH) + 1, yCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                    allSales.filter { it.date == yShamsi }
                }
                StatsDatePreset.WEEK -> {
                    val wCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -7) }
                    val wShamsi = PersianCalendarHelper.gregorianToShamsi(wCal.get(Calendar.YEAR), wCal.get(Calendar.MONTH) + 1, wCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                    allSales.filter { it.date >= wShamsi }
                }
                StatsDatePreset.LAST_WEEK -> {
                    val endCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -7) }
                    val startCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.DAY_OF_YEAR, -14) }
                    val startShamsi = PersianCalendarHelper.gregorianToShamsi(startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH) + 1, startCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                    val endShamsi = PersianCalendarHelper.gregorianToShamsi(endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH) + 1, endCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                    allSales.filter { it.date in startShamsi..endShamsi }
                }
                StatsDatePreset.MONTH -> {
                    val today = PersianCalendarHelper.getTodayShamsi()
                    val startOfMonth = PersianCalendarHelper.ShamsiDate(today.year, today.month, 1).formatPersian()
                    allSales.filter { it.date >= startOfMonth }
                }
                StatsDatePreset.LAST_MONTH -> {
                    val today = PersianCalendarHelper.getTodayShamsi()
                    val prevMonth = if (today.month == 1) 12 else today.month - 1
                    val prevYear = if (today.month == 1) today.year - 1 else today.year
                    val startOfPrev = PersianCalendarHelper.ShamsiDate(prevYear, prevMonth, 1).formatPersian()
                    val endOfPrev = PersianCalendarHelper.ShamsiDate(prevYear, prevMonth, PersianCalendarHelper.getDaysInShamsiMonth(prevYear, prevMonth)).formatPersian()
                    allSales.filter { it.date in startOfPrev..endOfPrev }
                }
                StatsDatePreset.THREE_MONTHS -> {
                    val mCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran")).apply { add(Calendar.MONTH, -3) }
                    val mShamsi = PersianCalendarHelper.gregorianToShamsi(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH) + 1, mCal.get(Calendar.DAY_OF_MONTH)).formatPersian()
                    allSales.filter { it.date >= mShamsi }
                }
            }
        }
    }

    val totalAmount = sales.sumOf { it.calc.k }
    val count = sales.size
    val avgAmount = if (count > 0) totalAmount / count else 0.0
    val maxAmount = sales.maxOfOrNull { it.calc.k } ?: 0.0

    // Chart Data Grouping
    val salesChartData = remember(sales) {
        val map = linkedMapOf<String, Double>()
        sales.reversed().forEach { item ->
            val date = item.date.ifEmpty { "ناشناس" }
            map[date] = (map[date] ?: 0.0) + item.calc.k
        }
        if (map.isEmpty()) listOf("امروز" to 0.0) else map.toList().takeLast(7)
    }

    val weightChartData = remember(sales) {
        val map = linkedMapOf<String, Double>()
        sales.reversed().forEach { item ->
            val date = item.date.ifEmpty { "ناشناس" }
            map[date] = (map[date] ?: 0.0) + item.calc.b
        }
        if (map.isEmpty()) listOf("امروز" to 0.0) else map.toList().takeLast(7)
    }

    if (showDatePickerDialog) {
        PersianDatePickerDialog(
            initialStartDate = customStartDate,
            initialEndDate = customEndDate,
            onConfirm = { sDate, eDate ->
                customStartDate = sDate
                customEndDate = eDate
                isCustomRangeActive = true
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Hero Total Sales Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = if (colors.isDark) {
                            listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
                        } else {
                            listOf(Color(0xFF172051), Color(0xFF2A3675), Color(0xFF3D4F96))
                        }
                    )
                )
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "مجموع فروش",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = scaledSp(13f),
                    fontFamily = VazirmatnFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = NumberFormatters.formatCurrency(totalAmount, toPersian = true).ifEmpty { "۰" },
                        color = Color.White,
                        fontSize = scaledSp(23f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تومان",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = scaledSp(12.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }

        // Date Range Selector Header & Calendar Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "بازه زمانی",
                fontSize = scaledSp(14.5f),
                fontFamily = VazirmatnFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            // Open Shamsi Calendar Button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { showDatePickerDialog = true }
                    .border(1.dp, if (isCustomRangeActive) colors.primary else colors.border, RoundedCornerShape(10.dp)),
                color = if (isCustomRangeActive) colors.primary.copy(alpha = 0.12f) else colors.surface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp), tint = colors.primary)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isCustomRangeActive) "$customStartDate تا $customEndDate" else "انتخاب از تقویم شمسی",
                        fontSize = scaledSp(11.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                }
            }
        }

        // Rich Quick Presets Scrollable Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(presetsScrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StatsDatePreset.values().forEach { preset ->
                val isSelected = !isCustomRangeActive && preset == activePreset
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            isCustomRangeActive = false
                            viewModel.setStatsDatePreset(preset)
                        }
                        .border(1.dp, if (isSelected) colors.primary else colors.border, RoundedCornerShape(10.dp)),
                    color = if (isSelected) colors.primary else colors.surface
                ) {
                    Text(
                        text = preset.title,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = scaledSp(11f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else colors.textPrimary
                    )
                }
            }
        }

        // KPI Summary Metric Cards (Count, Average, Max)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = "تعداد فروش",
                value = NumberFormatters.toPersianDigits(count.toString())
            )
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = "میانگین فروش",
                value = "${NumberFormatters.formatCurrency(avgAmount, toPersian = true).ifEmpty { "۰" }} ت"
            )
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = "بیشترین فروش",
                value = "${NumberFormatters.formatCurrency(maxAmount, toPersian = true).ifEmpty { "۰" }} ت"
            )
        }

        // Charts
        SimpleBarChart(
            title = "نمودار فروش (تومان)",
            data = salesChartData,
            color = colors.primary
        )

        SimpleLineChart(
            title = "نمودار وزن فروخته شده (گرم)",
            data = weightChartData,
            lineColor = colors.success
        )

        // Export Report Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "خروجی و گزارش‌گیری",
                    fontSize = scaledSp(13f),
                    fontFamily = VazirmatnFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val presetName = if (isCustomRangeActive) "$customStartDate تا $customEndDate" else activePreset.title
                            val report = """
                                گزارش فروش مهوا گالری
                                بازه: $presetName
                                ----------------------------
                                مجموع فروش: ${NumberFormatters.formatCurrency(totalAmount, toPersian = true)} تومان
                                تعداد تراکنش: ${NumberFormatters.toPersianDigits(count.toString())}
                                میانگین فروش: ${NumberFormatters.formatCurrency(avgAmount, toPersian = true)} تومان
                                بیشترین فروش: ${NumberFormatters.formatCurrency(maxAmount, toPersian = true)} تومان
                            """.trimIndent()
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, report)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "اشتراک متن گزارش"))
                        },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(15.dp), tint = colors.primary)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "متن", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.primary)
                    }

                    Button(
                        onClick = {
                            val presetName = if (isCustomRangeActive) "$customStartDate تا $customEndDate" else activePreset.title
                            ReceiptExporter.exportStatsPdf(
                                context = context,
                                sales = sales,
                                totalAmount = totalAmount,
                                count = count,
                                avgAmount = avgAmount,
                                maxAmount = maxAmount,
                                presetTitle = presetName
                            )
                        },
                        modifier = Modifier.weight(1.1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3675), contentColor = Color.White)
                    ) {
                        Icon(Icons.Filled.PictureAsPdf, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "خروجی PDF", fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = { viewModel.exportBackupJson(context) },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = Color.White)
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "داده‌ها", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun StatMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    val colors = AppTheme.colors
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = scaledSp(10f), fontFamily = VazirmatnFontFamily, color = colors.textMuted, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = value, fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily, color = colors.textPrimary, fontWeight = FontWeight.Bold)
        }
    }
}
