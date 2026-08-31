package com.mahvagallery.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun InputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    unitText: String,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Number,
    isUnitOnStart: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Row Label (Right side in RTL)
        Text(
            text = label,
            modifier = Modifier.weight(0.32f),
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Input Container (Left side in RTL)
        Box(
            modifier = Modifier
                .weight(0.68f)
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isReadOnly) DisabledBg else White)
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If unit is on the start (left in LTR)
                if (isUnitOnStart && unitText.isNotEmpty()) {
                    Text(
                        text = unitText,
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Text Field always displays numbers LTR
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp),
                        enabled = !isReadOnly,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        textStyle = TextStyle(
                            color = if (isReadOnly) PrimaryDark else TextDark,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        ),
                        cursorBrush = SolidColor(PrimaryDark)
                    )
                }

                // Default unit badge on the other end
                if (!isUnitOnStart && unitText.isNotEmpty()) {
                    Text(
                        text = unitText,
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
