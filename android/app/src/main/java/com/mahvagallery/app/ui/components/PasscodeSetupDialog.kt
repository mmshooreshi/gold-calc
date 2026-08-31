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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun PasscodeSetupDialog(
    onSetPasscode: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = AppTheme.colors
    var step by remember { mutableStateOf(1) } // 1: Enter PIN, 2: Confirm PIN
    var firstPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val currentPin = if (step == 1) firstPin else confirmPin

    fun onKeyPressed(digit: String) {
        errorMsg = null
        if (step == 1) {
            if (firstPin.length < 4) {
                firstPin += digit
                if (firstPin.length == 4) {
                    step = 2
                }
            }
        } else {
            if (confirmPin.length < 4) {
                confirmPin += digit
                if (confirmPin.length == 4) {
                    if (confirmPin == firstPin) {
                        onSetPasscode(firstPin)
                    } else {
                        errorMsg = "رمز عبور با تکرار آن مطابقت ندارد"
                        confirmPin = ""
                        step = 1
                        firstPin = ""
                    }
                }
            }
        }
    }

    fun onBackspace() {
        errorMsg = null
        if (step == 1) {
            if (firstPin.isNotEmpty()) firstPin = firstPin.dropLast(1)
        } else {
            if (confirmPin.isNotEmpty()) confirmPin = confirmPin.dropLast(1)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = colors.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
            shadowElevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
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
                        Icon(Icons.Filled.Lock, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (step == 1) "تعیین رمز عبور ۴ رقمی" else "تکرار رمز عبور",
                            fontSize = scaledSp(14.5f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(18.dp))
                    }
                }

                Text(
                    text = if (step == 1) "رمز عبور جدید خود را وارد کنید" else "جهت تایید، مجدداً رمز را وارد کنید",
                    fontSize = scaledSp(12f),
                    fontFamily = VazirmatnFontFamily,
                    color = colors.textMuted
                )

                if (errorMsg != null) {
                    Text(
                        text = errorMsg ?: "",
                        fontSize = scaledSp(11.5f),
                        fontFamily = VazirmatnFontFamily,
                        color = colors.danger,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 4-Dots
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0..3) {
                        val isFilled = i < currentPin.length
                        Box(
                            modifier = Modifier
                                .size(if (isFilled) 16.dp else 14.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isFilled) colors.primary
                                    else colors.inputBorder
                                )
                        )
                    }
                }

                // Numeric Keypad
                val keypad = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("cancel", "0", "back")
                )

                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    keypad.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            row.forEach { key ->
                                when (key) {
                                    "cancel" -> {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clickable(onClick = onDismiss),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "انصراف", fontSize = scaledSp(12f), color = colors.textMuted, fontFamily = VazirmatnFontFamily)
                                        }
                                    }
                                    "back" -> {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .clickable(onClick = ::onBackspace),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Filled.Backspace, contentDescription = "Delete", tint = colors.textPrimary, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                    else -> {
                                        Surface(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .clickable { onKeyPressed(key) },
                                            shape = CircleShape,
                                            color = colors.inputBgDisabled,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = NumberFormatters.toPersianDigits(key),
                                                    fontSize = scaledSp(20f),
                                                    fontFamily = VazirmatnFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colors.textPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
