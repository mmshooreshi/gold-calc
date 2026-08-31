package com.mahvagallery.app.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.ui.components.SimpleBarChart
import com.mahvagallery.app.ui.components.SimpleLineChart
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.utils.PersianCalendarHelper
import com.mahvagallery.app.utils.ReceiptExporter
import com.mahvagallery.app.viewmodel.ChartType
import com.mahvagallery.app.viewmodel.MainViewModel
import com.mahvagallery.app.viewmodel.StatsDatePreset
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    var isCustomRangeActive by remember { mutableStateOf(false) }
    var customStartDate by remember { mutableStateOf("") }
    var customEndDate by remember { mutableStateOf("") }

    // Filter sales by preset or custom date range
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
                    val cal = Calendar.getInstance()
                    val todayIso = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
                    allSales.filter { it.iso == todayIso }
                }
                StatsDatePreset.WEEK -> {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DAY_OF_YEAR, -7)
                    val cutoff = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
                    allSales.filter { it.iso >= cutoff }
                }
                StatsDatePreset.MONTH -> {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.MONTH, -1)
                    val cutoff = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
                    allSales.filter { it.iso >= cutoff }
                }
                StatsDatePreset.THREE_MONTHS -> {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.MONTH, -3)
                    val cutoff = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
                    allSales.filter { it.iso >= cutoff }
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                .padding(20.dp),
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
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = NumberFormatters.formatCurrency(totalAmount, toPersian = true).ifEmpty { "۰" },
                        color = Color.White,
                        fontSize = scaledSp(24f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تومان",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = scaledSp(13f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }

        // Date Range Selector Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "بازه زمانی",
                fontSize = scaledSp(15f),
                fontFamily = VazirmatnFontFamily,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )

            // Custom Range Toggle Button
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isCustomRangeActive = !isCustomRangeActive }
                    .border(1.dp, if (isCustomRangeActive) colors.primary else colors.border, RoundedCornerShape(8.dp)),
                color = if (isCustomRangeActive) colors.primary.copy(alpha = 0.12f) else colors.surface
            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DateRange, contentDescription = null, modifier = Modifier.size(14.dp), tint = colors.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "بازه دلخواه",
                        fontSize = scaledSp(11.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                }
            }
        }

        // Custom Date Range Inputs
        AnimatedVisibility(
            visible = isCustomRangeActive,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomDateInput(
                        modifier = Modifier.weight(1f),
                        label = "از تاریخ",
                        value = customStartDate,
                        onValueChange = { customStartDate = it },
                        placeholder = "۱۴۰۳/۰۱/۰۱"
                    )

                    CustomDateInput(
                        modifier = Modifier.weight(1f),
                        label = "تا تاریخ",
                        value = customEndDate,
                        onValueChange = { customEndDate = it },
                        placeholder = PersianCalendarHelper.getTodayShamsi().formatPersian()
                    )
                }
            }
        }

        // Standard Date Presets Row
        if (!isCustomRangeActive) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatsDatePreset.values().forEach { preset ->
                    val isSelected = preset == activePreset
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setStatsDatePreset(preset) }
                            .border(1.dp, if (isSelected) colors.primary else colors.border, RoundedCornerShape(12.dp)),
                        color = if (isSelected) colors.primary else colors.surface
                    ) {
                        Text(
                            text = preset.title,
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontSize = scaledSp(11f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else colors.textPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // KPI Summary Metric Cards (Count, Average, Max)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            shape = RoundedCornerShape(14.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CustomDateInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = label, fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, color = colors.textMuted)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
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
                textStyle = TextStyle(
                    fontFamily = VazirmatnFontFamily,
                    color = colors.textPrimary,
                    fontSize = scaledSp(12f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    textDirection = TextDirection.Ltr
                ),
                cursorBrush = SolidColor(colors.primary),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = colors.textMuted.copy(alpha = 0.4f), fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily)
                    }
                    innerTextField()
                }
            )
        }
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
        shape = RoundedCornerShape(12.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = scaledSp(10.5f), fontFamily = VazirmatnFontFamily, color = colors.textMuted, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily, color = colors.textPrimary, fontWeight = FontWeight.Bold)
        }
    }
}
