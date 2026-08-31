package com.mahvagallery.app.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sparkles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.components.DebugLogViewer
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isDark by viewModel.isDarkMode.collectAsState()
    val isBold by viewModel.isBoldText.collectAsState()
    val fontScaleDelta by viewModel.fontScaleDelta.collectAsState()
    val defaults by viewModel.defaults.collectAsState()
    val logs by viewModel.logs.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var defD by remember(defaults) { mutableStateOf(defaults.defaultD) }
    var defF by remember(defaults) { mutableStateOf(defaults.defaultF) }
    var defH by remember(defaults) { mutableStateOf(defaults.defaultH) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "تنظیمات و ظاهر",
            fontSize = 17.sp,
            fontFamily = VazirmatnFontFamily,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // Live Real-Time Interactive Preview Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryDark.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Sparkles, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "پیش‌نمایش زنده قلم و اندازه",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    }
                    Text(
                        text = "اندازه: ${NumberFormatters.toPersianDigits((14 + fontScaleDelta).toString())}px",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "نمونه قیمت طلا:",
                        fontSize = (13.5 + fontScaleDelta).sp,
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
                        color = TextDark
                    )
                    Text(
                        text = "${NumberFormatters.toPersianDigits("14,850,000")} تومان",
                        fontSize = (14.5 + fontScaleDelta).sp,
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Black,
                        color = PrimaryDark
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "وزن کل (گرم):",
                        fontSize = (12.5 + fontScaleDelta).sp,
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        color = TextMuted
                    )
                    Text(
                        text = "${NumberFormatters.toPersianDigits("3.420")} گرم",
                        fontSize = (13 + fontScaleDelta).sp,
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }

        // Group 1: Appearance & Typography
        SettingsGroup(title = "ظاهر و قلم برنامه") {
            // Dark Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDarkMode() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Nightlight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "حالت تاریک (Dark Mode)", fontSize = 13.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Color(0xFF10B981))
                )
            }

            Divider(color = BorderColor)

            // Bold Text Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleBoldText() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.FormatBold, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "متن ضخیم (Bold)", fontSize = 13.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Switch(
                    checked = isBold,
                    onCheckedChange = { viewModel.toggleBoldText() },
                    colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Color(0xFF10B981))
                )
            }

            Divider(color = BorderColor)

            // Font Size Controls (+ / -)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.FormatSize, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "اندازه فونت", fontSize = 13.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = TextDark)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.changeFontScale(-1) }
                            .border(1.dp, BorderColor, RoundedCornerShape(10.dp)),
                        color = DisabledBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                    }

                    Text(
                        text = "${NumberFormatters.toPersianDigits((14 + fontScaleDelta).toString())}px",
                        fontSize = 13.5.sp,
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark,
                        modifier = Modifier.width(42.dp),
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.changeFontScale(1) }
                            .border(1.dp, BorderColor, RoundedCornerShape(10.dp)),
                        color = DisabledBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                    }
                }
            }

            Divider(color = BorderColor)

            // Vazirmatn Typography Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.TextFields, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "قلم اصلی فارسی", fontSize = 13.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrimaryDark.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "وزیرمتن (Vazirmatn)", fontSize = 11.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = PrimaryDark)
                }
            }
        }

        // Group 2: Default Percentages
        SettingsGroup(title = "مقادیر پیش‌فرض درصدها") {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DefaultPercentInputRow(label = "اجرت پیش‌فرض ٪", value = defD, onValueChange = { defD = it }, placeholder = "مثلاً ۷")
                DefaultPercentInputRow(label = "سود پیش‌فرض ٪", value = defF, onValueChange = { defF = it }, placeholder = "مثلاً ۷")
                DefaultPercentInputRow(label = "مالیات پیش‌فرض ٪", value = defH, onValueChange = { defH = it }, placeholder = "مثلاً ۹")

                Button(
                    onClick = { viewModel.saveDefaultPercentages(defD, defF, defH) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = White)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "ذخیره و اعمال مقادیر پیش‌فرض", fontSize = 13.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Group 3: Data Management
        SettingsGroup(title = "مدیریت داده‌ها و پشتیبان‌گیری") {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.exportBackupJson(context) },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryDark)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "دانلود JSON", fontSize = 12.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    }

                    OutlinedButton(
                        onClick = { viewModel.importBackupJson(context) },
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.FileUpload, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryDark)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "وارد کردن", fontSize = 12.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    }
                }

                Button(
                    onClick = viewModel::showClearAllDataConfirmation,
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.1f), contentColor = Color(0xFFEF4444))
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "پاک‌سازی تمام داده‌ها", fontSize = 12.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Group 4: Live Debug Console
        SettingsGroup(title = "کنسول دیباگ زنده (Debug Logs)") {
            DebugLogViewer(
                logs = logs,
                onClearLogs = viewModel::clearDebugLogs
            )
        }

        // App Version
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "نسخه ۲.۰.۰ — گالری مهوا",
                fontSize = 11.sp,
                fontFamily = VazirmatnFontFamily,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontFamily = VazirmatnFontFamily,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = White,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
            shadowElevation = 1.dp
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun DefaultPercentInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, fontFamily = VazirmatnFontFamily, color = TextDark, fontWeight = FontWeight.Medium)

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DisabledBg)
                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = TextStyle(
                        fontFamily = VazirmatnFontFamily,
                        color = TextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        textDirection = TextDirection.Rtl
                    ),
                    cursorBrush = SolidColor(PrimaryDark),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(text = placeholder, color = Color(0xFFBAC5D6), fontSize = 12.sp, fontFamily = VazirmatnFontFamily)
                        }
                        innerTextField()
                    }
                )
                Text(text = "٪", color = TextMuted, fontSize = 10.5.sp, fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Medium)
            }
        }
    }
}
