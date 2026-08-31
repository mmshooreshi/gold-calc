package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.R
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.DisabledBg
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.TextMuted
import com.mahvagallery.app.ui.theme.White

@Composable
fun SplitInputRow(
    label: String,
    percentValue: String,
    onPercentChange: (String) -> Unit,
    amountValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Row Label (Right in RTL)
        Text(
            text = label,
            modifier = Modifier.weight(0.32f),
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Split Container (Left in RTL)
        Row(
            modifier = Modifier.weight(0.68f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Box 1: Percentage (Editable)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .border(
                        width = 1.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp),
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
                                .padding(horizontal = 4.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(
                                color = TextDark,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            cursorBrush = SolidColor(PrimaryDark)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.unit_percent),
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Box 2: Calculated Amount (Disabled / Read-only)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DisabledBg)
                    .border(
                        width = 1.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 8.dp),
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
                                .padding(horizontal = 4.dp),
                            enabled = false,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = PrimaryDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.unit_toman),
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
