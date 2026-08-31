package com.mahvagallery.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.mahvagallery.app.ui.components.DebugLogViewer
import com.mahvagallery.app.ui.components.PasscodeSetupDialog
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters
import com.mahvagallery.app.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isDark by viewModel.isDarkMode.collectAsState()
    val isBold by viewModel.isBoldText.collectAsState()
    val fontScaleDelta by viewModel.fontScaleDelta.collectAsState()
    val defaults by viewModel.defaults.collectAsState()
    val snapshots by viewModel.snapshots.collectAsState()
    val isPasscodeOn by viewModel.isPasscodeEnabled.collectAsState()
    val showPasscodeSetup by viewModel.showPasscodeSetupDialog.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val colors = AppTheme.colors

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var defD by remember(defaults) { mutableStateOf(defaults.defaultD) }
    var defF by remember(defaults) { mutableStateOf(defaults.defaultF) }
    var defH by remember(defaults) { mutableStateOf(defaults.defaultH) }

    var sliderValue by remember(fontScaleDelta) { mutableFloatStateOf(fontScaleDelta.toFloat()) }

    // Auto-update current snapshot on screen load
    LaunchedEffect(Unit) {
        viewModel.repository.autoUpdateCurrentSnapshot()
    }

    // Real JSON File Picker
    val jsonFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importBackupFromUri(context, it) }
    }

    if (showPasscodeSetup) {
        PasscodeSetupDialog(
            onSetPasscode = viewModel::setPasscode,
            onDismiss = viewModel::closePasscodeSetup
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
        Text(
            text = "تنظیمات و امنیت برنامه",
            fontSize = scaledSp(16.5f),
            fontFamily = VazirmatnFontFamily,
            fontWeight = FontWeight.Bold,
            color = colors.primary,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        // Live Real-Time Interactive Preview Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary.copy(alpha = 0.4f)),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = colors.warning, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "پیش‌نمایش زنده قلم و اندازه",
                            fontSize = scaledSp(13f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.success.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "اندازه: ${NumberFormatters.toPersianDigits((14 + fontScaleDelta).toString())}px",
                            fontSize = scaledSp(11.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.success
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نمونه قیمت طلا:",
                        fontSize = scaledSp(13f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "${NumberFormatters.toPersianDigits("14,850,000")} تومان",
                        fontSize = scaledSp(14.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Black,
                        color = colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "وزن کل (گرم):",
                        fontSize = scaledSp(12f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        color = colors.textMuted
                    )
                    Text(
                        text = "${NumberFormatters.toPersianDigits("3.420")} گرم",
                        fontSize = scaledSp(12.5f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
            }
        }

        // Group 1: Security & Passcode
        SettingsGroup(title = "امنیت و قفل برنامه (مشابه تلگرام)") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPasscodeOn) viewModel.disablePasscode()
                        else viewModel.openPasscodeSetup()
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "رمز عبور ورود به برنامه", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                        Text(text = if (isPasscodeOn) "فعال با رمز ۴ رقمی" else "غیرفعال", fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, color = colors.textMuted)
                    }
                }

                Switch(
                    checked = isPasscodeOn,
                    onCheckedChange = { checked ->
                        if (checked) viewModel.openPasscodeSetup()
                        else viewModel.disablePasscode()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colors.success,
                        uncheckedThumbColor = colors.textMuted,
                        uncheckedTrackColor = colors.inputBorder
                    )
                )
            }

            if (isPasscodeOn) {
                HorizontalDivider(color = colors.divider)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openPasscodeSetup() }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Key, contentDescription = null, tint = colors.warning, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "تغییر رمز عبور", fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.warning)
                    }
                    Text(text = "ویرایش ➔", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, color = colors.warning, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Group 2: Appearance & Typography
        SettingsGroup(title = "ظاهر و قلم برنامه") {
            // Dark Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDarkMode() }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Nightlight, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(19.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "حالت تاریک (Dark Mode)", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colors.success,
                        uncheckedThumbColor = colors.textMuted,
                        uncheckedTrackColor = colors.inputBorder
                    )
                )
            }

            HorizontalDivider(color = colors.divider)

            // Bold Text Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleBoldText() }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.FormatBold, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(19.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "متن ضخیم (Bold)", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                }
                Switch(
                    checked = isBold,
                    onCheckedChange = { viewModel.toggleBoldText() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colors.success,
                        uncheckedThumbColor = colors.textMuted,
                        uncheckedTrackColor = colors.inputBorder
                    )
                )
            }

            HorizontalDivider(color = colors.divider)

            // Dynamic Font Size Slider (Huge range: -4 to +10)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.FormatSize, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(19.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "نوار اندازه قلم برنامه", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                    }

                    Text(
                        text = "${NumberFormatters.toPersianDigits((14 + sliderValue.roundToInt()).toString())}px",
                        fontSize = scaledSp(13f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                }

                Slider(
                    value = sliderValue,
                    onValueChange = { newVal ->
                        sliderValue = newVal
                        val intDelta = newVal.roundToInt()
                        if (intDelta != fontScaleDelta) {
                            viewModel.changeFontScale(intDelta - fontScaleDelta)
                        }
                    },
                    valueRange = -4f..10f,
                    steps = 13,
                    colors = SliderDefaults.colors(
                        thumbColor = colors.primary,
                        activeTrackColor = colors.primary,
                        inactiveTrackColor = colors.inputBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Quick preset buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val presets = listOf(
                        "بسیار ریز" to -3,
                        "استاندارد" to 0,
                        "درشت" to 4,
                        "خیلی بزرگ" to 8
                    )
                    for ((pLabel, pVal) in presets) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    sliderValue = pVal.toFloat()
                                    viewModel.changeFontScale(pVal - fontScaleDelta)
                                }
                                .border(1.dp, if (fontScaleDelta == pVal) colors.primary else colors.border, RoundedCornerShape(8.dp)),
                            color = if (fontScaleDelta == pVal) colors.primary.copy(alpha = 0.15f) else colors.inputBgDisabled
                        ) {
                            Text(
                                text = pLabel,
                                fontSize = scaledSp(10.5f),
                                fontFamily = VazirmatnFontFamily,
                                fontWeight = if (fontScaleDelta == pVal) FontWeight.Bold else FontWeight.Normal,
                                color = if (fontScaleDelta == pVal) colors.primary else colors.textMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = colors.divider)

            // Vazirmatn Typography Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.TextFields, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(19.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "قلم اصلی برنامه", fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.primary.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(text = "وزیرمتن (Vazirmatn)", fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.primary)
                }
            }
        }

        // Group 3: Default Percentages
        SettingsGroup(title = "مقادیر پیش‌فرض درصدها") {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                DefaultPercentInputRow(label = "اجرت پیش‌فرض ٪", value = defD, onValueChange = { defD = it }, placeholder = "مثلاً ۷")
                DefaultPercentInputRow(label = "سود پیش‌فرض ٪", value = defF, onValueChange = { defF = it }, placeholder = "مثلاً ۷")
                DefaultPercentInputRow(label = "مالیات پیش‌فرض ٪", value = defH, onValueChange = { defH = it }, placeholder = "مثلاً ۹")

                Button(
                    onClick = { viewModel.saveDefaultPercentages(defD, defF, defH) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = Color.White)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "ذخیره و اعمال مقادیر پیش‌فرض", fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Group 4: Snapshot Version History & Backup
        SettingsGroup(title = "نسخه‌های ذخیره‌شده و پشتیبان‌گیری") {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Actions row: Export JSON & Import JSON (Real file picker!)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.exportBackupJson(context) },
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3675), contentColor = Color.White)
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "دانلود JSON", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = { jsonFilePickerLauncher.launch("application/json") },
                        modifier = Modifier.weight(1.1f).height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = Color.White)
                    ) {
                        Icon(Icons.Filled.FileUpload, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "انتخاب و وارد کردن", fontSize = scaledSp(11.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Snapshots list header + Create snapshot button
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "تاریخچه نسخه‌های پشتیبان:",
                        fontSize = scaledSp(12f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = colors.textMuted
                    )

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.repository.createSnapshot() }
                            .border(1.dp, colors.primary.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                        color = colors.primary.copy(alpha = 0.1f)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(13.dp), tint = colors.primary)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "ثبت نسخه جدید", fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.primary)
                        }
                    }
                }

                // Snapshot compact rows
                if (snapshots.isEmpty()) {
                    Text(text = "نسخه‌ای ثبت نشده است", fontSize = scaledSp(11.5f), color = colors.textMuted)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        snapshots.forEach { snap ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = if (snap.isCurrent) colors.primary.copy(alpha = 0.08f) else colors.inputBgDisabled,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (snap.isCurrent) colors.primary else colors.border)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (snap.isCurrent) {
                                            Box(
                                                modifier = Modifier
                                                    .size(7.dp)
                                                    .clip(CircleShape)
                                                    .background(colors.success)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "${snap.date}  |  ${NumberFormatters.toPersianDigits(snap.time)}",
                                                fontSize = scaledSp(11.5f),
                                                fontFamily = VazirmatnFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.textPrimary
                                            )
                                            Text(
                                                text = "${NumberFormatters.toPersianDigits(snap.itemCount.toString())} تراکنش",
                                                fontSize = scaledSp(10.5f),
                                                fontFamily = VazirmatnFontFamily,
                                                color = colors.textMuted
                                            )
                                        }
                                    }

                                    if (snap.isCurrent) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(colors.success.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(text = "نسخه فعلی ✓", fontSize = scaledSp(10.5f), fontFamily = VazirmatnFontFamily, color = colors.success, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { viewModel.restoreSnapshot(snap.id) }
                                                .border(1.dp, colors.border, RoundedCornerShape(8.dp)),
                                            color = colors.surface
                                        ) {
                                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Filled.Restore, contentDescription = null, modifier = Modifier.size(13.dp), tint = colors.primary)
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(text = "بازیابی", fontSize = scaledSp(11f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = colors.primary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Clear All Data Button with crisp styling
                Button(
                    onClick = viewModel::showClearAllDataConfirmation,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp).height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.danger, contentColor = Color.White)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "پاک‌سازی تمام داده‌ها", fontSize = scaledSp(12.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Group 5: Live Debug Console
        SettingsGroup(title = "کنسول دیباگ زنده (Debug Logs)") {
            DebugLogViewer(
                logs = logs,
                onCopyLogs = { viewModel.copyLogsToClipboard(context) },
                onClearLogs = viewModel::clearDebugLogs
            )
        }

        // App Version
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "نسخه ۲.۰.۰ — گالری مهوا",
                fontSize = scaledSp(11f),
                fontFamily = VazirmatnFontFamily,
                color = colors.textMuted,
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
    val colors = AppTheme.colors
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = title,
            fontSize = scaledSp(12.5f),
            fontFamily = VazirmatnFontFamily,
            fontWeight = FontWeight.Bold,
            color = colors.textMuted,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
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
    val colors = AppTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = scaledSp(13f), fontFamily = VazirmatnFontFamily, color = colors.textPrimary, fontWeight = FontWeight.Medium)

        Box(
            modifier = Modifier
                .width(96.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.inputBgDisabled)
                .border(1.dp, colors.inputBorder, RoundedCornerShape(8.dp))
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
                        color = colors.textPrimary,
                        fontSize = scaledSp(13f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        textDirection = TextDirection.Ltr
                    ),
                    cursorBrush = SolidColor(colors.primary),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(text = placeholder, color = colors.textMuted.copy(alpha = 0.5f), fontSize = scaledSp(12f), fontFamily = VazirmatnFontFamily)
                        }
                        innerTextField()
                    }
                )
                Text(text = "٪", color = colors.textMuted, fontSize = scaledSp(10.5f), fontFamily = VazirmatnFontFamily, fontWeight = FontWeight.Medium)
            }
        }
    }
}
