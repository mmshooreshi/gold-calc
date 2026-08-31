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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White

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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Label with info icon
        Row(
            modifier = Modifier.weight(0.32f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = TextDark,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )
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

        // Split inputs + lock
        Row(
            modifier = Modifier.weight(0.68f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Box 1: Percentage input
            Box(
                modifier = Modifier
                    .weight(0.42f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .border(
                        width = 1.2.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BasicTextField(
                            value = percentValue,
                            onValueChange = onPercentChange,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(
                                color = TextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            cursorBrush = SolidColor(PrimaryDark)
                        )
                    }
                    Text(
                        text = "٪",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Box 2: Calculated Amount
            Box(
                modifier = Modifier
                    .weight(0.58f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DisabledBg)
                    .border(
                        width = 1.2.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
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
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    Text(
                        text = "تومان",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Lock Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onToggleLock() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = "Lock",
                    modifier = Modifier.size(17.dp),
                    tint = if (isLocked) MaterialTheme.colorScheme.primary else Color(0xFFBCC9DF)
                )
            }
        }
    }
}
