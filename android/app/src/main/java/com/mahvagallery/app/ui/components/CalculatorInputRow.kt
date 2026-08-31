package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

@Composable
fun CalculatorInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    unitText: String = "",
    isReadOnly: Boolean = false,
    isHighlighted: Boolean = false,
    isLocked: Boolean? = null,
    onToggleLock: (() -> Unit)? = null,
    onInfoClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val isItemLocked = isLocked == true

    var tfvState by remember(value) {
        val cursor = if (value.isNotEmpty()) value.length else 0
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(cursor)))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label with optional info (RTL)
        Row(
            modifier = Modifier.weight(0.32f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = when {
                    isHighlighted -> colors.primary
                    isItemLocked -> colors.lockedText
                    else -> colors.textPrimary
                },
                fontSize = scaledSp(13.5f),
                fontFamily = VazirmatnFontFamily,
                fontWeight = if (isHighlighted || isItemLocked) FontWeight.Bold else FontWeight.SemiBold
            )
            if (onInfoClick != null) {
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
        }

        // Input container + lock
        Row(
            modifier = Modifier.weight(0.68f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val bg = when {
                isItemLocked -> colors.lockedBg
                isHighlighted -> colors.highlightBg
                isReadOnly -> colors.inputBgDisabled
                else -> colors.inputBg
            }
            val borderClr = when {
                isItemLocked -> colors.lockedBorder
                isHighlighted -> colors.highlightBorder
                else -> colors.inputBorder
            }
            val borderWidth = when {
                isItemLocked || isHighlighted -> 1.8.dp
                else -> 1.dp
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .border(borderWidth, borderClr, RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val textColor = when {
                        isItemLocked -> colors.lockedText
                        isHighlighted -> colors.primary
                        isReadOnly -> colors.textSecondary
                        else -> colors.textPrimary
                    }

                    // Number Input Field in LTR so typing flows left-to-right and cursor stays at right!
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BasicTextField(
                            value = if (isReadOnly) TextFieldValue(value) else tfvState,
                            onValueChange = { newTfv ->
                                tfvState = newTfv
                                if (newTfv.text != value) {
                                    onValueChange(newTfv.text)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            enabled = !isReadOnly,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                            textStyle = TextStyle(
                                fontFamily = VazirmatnFontFamily,
                                color = textColor,
                                fontSize = if (isHighlighted) scaledSp(16.5f) else scaledSp(14.5f),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                textDirection = TextDirection.Ltr
                            ),
                            cursorBrush = SolidColor(if (isItemLocked) colors.lockedBorder else colors.primary),
                            decorationBox = { innerTextField ->
                                if (value.isEmpty() && placeholder.isNotEmpty()) {
                                    Text(
                                        text = placeholder,
                                        color = colors.textMuted.copy(alpha = 0.5f),
                                        fontSize = scaledSp(14f),
                                        fontFamily = VazirmatnFontFamily
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    if (unitText.isNotEmpty()) {
                        Text(
                            text = unitText,
                            color = if (isItemLocked) colors.lockedText else if (isHighlighted) colors.primary else colors.textMuted,
                            fontSize = scaledSp(11f),
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Lock icon
            if (isLocked != null && onToggleLock != null) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clickable { onToggleLock() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isItemLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                        contentDescription = "Lock",
                        modifier = Modifier.size(18.dp),
                        tint = if (isItemLocked) colors.lockedBorder else colors.textMuted
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(34.dp))
            }
        }
    }
}
