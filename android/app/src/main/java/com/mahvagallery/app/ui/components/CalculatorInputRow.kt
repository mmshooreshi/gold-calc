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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.VazirmatnFontFamily
import com.mahvagallery.app.ui.theme.White

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
    var textFieldValueState by remember(value) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label with optional info icon
        Row(
            modifier = Modifier.weight(0.32f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = if (isHighlighted) MaterialTheme.colorScheme.primary else TextDark,
                fontSize = 13.5.sp,
                fontFamily = VazirmatnFontFamily,
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold
            )
            if (onInfoClick != null) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info",
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { onInfoClick() },
                    tint = TextMuted
                )
            }
        }

        // Input container + lock
        Row(
            modifier = Modifier.weight(0.68f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isReadOnly) DisabledBg else White)
                    .border(
                        width = if (isHighlighted) 2.dp else 1.2.dp,
                        color = if (isHighlighted) MaterialTheme.colorScheme.primary else BorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = if (isReadOnly) TextFieldValue(value) else textFieldValueState,
                        onValueChange = { newTfv ->
                            textFieldValueState = newTfv
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
                            color = if (isReadOnly) MaterialTheme.colorScheme.primary else TextDark,
                            fontSize = if (isHighlighted) 16.sp else 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            textDirection = TextDirection.Rtl
                        ),
                        cursorBrush = SolidColor(PrimaryDark),
                        decorationBox = { innerTextField ->
                            if (value.isEmpty() && placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = Color(0xFFC4CFDE),
                                    fontSize = 14.sp,
                                    fontFamily = VazirmatnFontFamily
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (unitText.isNotEmpty()) {
                        Text(
                            text = unitText,
                            color = if (isHighlighted) MaterialTheme.colorScheme.primary else TextMuted,
                            fontSize = 11.sp,
                            fontFamily = VazirmatnFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Lock icon or spacing spacer
            if (isLocked != null && onToggleLock != null) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onToggleLock() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                        contentDescription = "Lock",
                        modifier = Modifier.size(16.dp),
                        tint = if (isLocked) PrimaryDark else TextMuted
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}
