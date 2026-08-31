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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.components.DebugLogViewer
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
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
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "تنظیمات",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // Group 1: Appearance & Typography
        SettingsGroup(title = "ظاهر و فونت") {
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
                    Text(text = "حالت تاریک", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
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
                    Text(text = "متن ضخیم (Bold)", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Switch(
                    checked = isBold,
                    onCheckedChange = { viewModel.toggleBoldText() },
                    colors = SwitchDefaults.colors(checkedThumbColor = White, checkedTrackColor = Color(0xFF10B981))
                )
            }

            Divider(color = BorderColor)

            // Font Size Controls
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
                    Text(text = "اندازه فونت", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.changeFontScale(-1) }
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                        color = DisabledBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                    }

                    Text(
                        text = "${NumberFormatters.toPersianDigits((14 + fontScaleDelta).toString())}px",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.changeFontScale(1) }
                            .border(1.dp, BorderColor, RoundedCornerShape(8.dp)),
                        color = DisabledBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                    }
                }
            }

            Divider(color = BorderColor)

            // Dedicated Vazirmatn Typography Badge
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
                    Text(text = "قلم استاندارد", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(PrimaryDark.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "وزیرمتن (Vazirmatn)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                }
            }
        }

        // Group 2: Default Values
        SettingsGroup(title = "مقادیر پیش‌فرض") {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DefaultPercentInputRow(label = "اجرت پیش‌فرض ٪", value = defD, onValueChange = { defD = it }, placeholder = "مثلاً ۷")
                DefaultPercentInputRow(label = "سود پیش‌فرض ٪", value = defF, onValueChange = { defF = it }, placeholder = "مثلاً ۱۲")
                DefaultPercentInputRow(label = "مالیات پیش‌فرض ٪", value = defH, onValueChange = { defH = it }, placeholder = "مثلاً ۹")

                Button(
                    onClick = { viewModel.saveDefaultPercentages(defD, defF, defH) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = White)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "ذخیره پیش‌فرض‌ها", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Group 3: Data Management & Backup
        SettingsGroup(title = "مدیریت داده‌ها") {
            // Export Backup
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val json = viewModel.repository.exportBackupJson()
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, json)
                            type = "application/json"
                        }
                        context.startActivity(Intent.createChooser(intent, "پشتیبان‌گیری JSON"))
                    }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.FileDownload, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "خروجی فایل پشتیبان (JSON)", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
            }

            Divider(color = BorderColor)

            // Clear All Data
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.requestClearAllData() }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Delete, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "پاک‌سازی کامل تمام داده‌ها", fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                }
                Icon(Icons.Filled.ChevronLeft, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
            }
        }

        // Group 4: Live Debug Logs Viewer
        DebugLogViewer(
            logs = logs,
            onCopyLogs = { viewModel.copyLogsToClipboard(context) },
            onClearLogs = viewModel::clearDebugLogs
        )

        // Group 5: About App & Version
        SettingsGroup(title = "درباره") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "نسخه برنامه", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                }
                Text(text = "6.0.0 (Native Release)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.padding(start = 6.dp, bottom = 4.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
            color = White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
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
        Text(text = label, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = TextDark, modifier = Modifier.width(110.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DisabledBg)
                .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = TextStyle(
                    color = TextDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                cursorBrush = SolidColor(PrimaryDark),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = Color(0xFFC4CFDE), fontSize = 12.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}
