package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mahvagallery.app.ui.theme.AppTheme
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.scaledSp
import com.mahvagallery.app.utils.NumberFormatters

@Composable
fun SplitCalculationRow(
    label: String,
    percentValue: String,
    onPercentChange: (String) -> Unit,
    amountValue: String,
    isLocked: Boolean,
    onToggleLock: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    var tfvState by remember {
        mutableStateOf(TextFieldValue(text = percentValue, selection = TextRange(percentValue.length)))
    }

    LaunchedEffect(percentValue) {
        if (percentValue != tfvState.text) {
            val cursor = if (percentValue.isNotEmpty()) percentValue.length else 0
            tfvState = TextFieldValue(text = percentValue, selection = TextRange(cursor))
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label with info
        Row(
            modifier = Modifier.weight(0.32f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = if (isLocked) colors.lockedText else colors.textPrimary,
                fontSize = scaledSp(13.5f),
                fontFamily = VazirmatnFontFamily,
                fontWeight = if (isLocked) FontWeight.Bold else FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Info",
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onInfoClick() },
                tint = colors.textMuted
            )
        }

        // Split inputs + lock
        Row(
            modifier = Modifier.weight(0.68f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Box 1: Percentage input (LTR with preserved cursor!)
            val percentBg = if (isLocked) colors.lockedBg else colors.inputBg
            val percentBorder = if (isLocked) colors.lockedBorder else colors.inputBorder
            val percentTextColor = if (isLocked) colors.lockedText else colors.textPrimary

            Box(
                modifier = Modifier
                    .weight(0.42f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(percentBg)
                    .border(if (isLocked) 1.8.dp else 1.dp, percentBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BasicTextField(
                            value = tfvState,
                            onValueChange = { newTfv ->
                                if (newTfv.text == tfvState.text) {
                                    tfvState = newTfv
                                } else {
                                    val formatted = NumberFormatters.formatPercentageInput(newTfv.text)
                                    val newCursorPos = NumberFormatters.calculateCursorPosition(
                                        newRawText = newTfv.text,
                                        rawCursor = newTfv.selection.end,
                                        formattedText = formatted
                                    )
                                    tfvState = TextFieldValue(
                                        text = formatted,
                                        selection = TextRange(newCursorPos)
                                    )
                                    onPercentChange(formatted)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(
                                fontFamily = VazirmatnFontFamily,
                                color = percentTextColor,
                                fontSize = scaledSp(14f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                textDirection = TextDirection.Ltr
                            ),
                            cursorBrush = SolidColor(if (isLocked) colors.lockedBorder else colors.primary)
                        )
                    }
                    Text(
                        text = "٪",
                        color = if (isLocked) colors.lockedText else colors.textMuted,
                        fontSize = scaledSp(11f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Box 2: Calculated Amount (LTR)
            Box(
                modifier = Modifier
                    .weight(0.58f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.inputBgDisabled)
                    .border(1.dp, colors.inputBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BasicTextField(
                            value = amountValue,
                            onValueChange = {},
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp),
                            enabled = false,
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = VazirmatnFontFamily,
                                color = colors.primary,
                                fontSize = scaledSp(13.5f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                textDirection = TextDirection.Ltr
                            )
                        )
                    }
                    Text(
                        text = "تومان",
                        color = colors.textMuted,
                        fontSize = scaledSp(10f),
                        fontFamily = VazirmatnFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Lock icon
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onToggleLock() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = "Lock",
                    modifier = Modifier.size(18.dp),
                    tint = if (isLocked) colors.lockedBorder else colors.textMuted
                )
            }
        }
    }
}
