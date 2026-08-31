package com.mahvagallery.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahvagallery.app.ui.components.CalculatorInputRow
import com.mahvagallery.app.ui.components.SplitCalculationRow
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.TextDark
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.viewmodel.MainViewModel

@Composable
fun CalculatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.calcState.collectAsState()
    val locks by viewModel.locks.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Edit Mode Banner
        AnimatedVisibility(
            visible = state.editingId != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF59E0B))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "در حال ویرایش تراکنش",
                            color = White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.cancelEdit() },
                        color = White.copy(alpha = 0.25f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null, tint = White, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "لغو", color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Row 1: Gold Price
        CalculatorInputRow(
            label = "قیمت طلا",
            value = state.goldPrice,
            onValueChange = viewModel::onGoldPriceChange,
            unitText = "تومان",
            keyboardType = KeyboardType.Number,
            isLocked = locks.lockA,
            onToggleLock = { viewModel.toggleLock("A") },
            placeholder = "۰"
        )

        // Row 2: Weight
        CalculatorInputRow(
            label = "وزن",
            value = state.weight,
            onValueChange = viewModel::onWeightChange,
            unitText = "گرم",
            keyboardType = KeyboardType.Decimal,
            placeholder = "۰.۰۰۰"
        )

        // Row 3: Raw Price
        CalculatorInputRow(
            label = "قیمت خام",
            value = state.rawPrice,
            onValueChange = {},
            unitText = "تومان",
            isReadOnly = true
        )

        // Row 4: Ojrat
        SplitCalculationRow(
            label = "اجرت",
            percentValue = state.ojratPercent,
            onPercentChange = viewModel::onOjratPercentChange,
            amountValue = state.ojratAmount,
            isLocked = locks.lockD,
            onToggleLock = { viewModel.toggleLock("D") },
            onInfoClick = {
                viewModel.showInfo("اجرت ساخت", "درصد اجرت ساخت طلا روی قیمت خام محاسبه می‌شود.")
            }
        )

        // Row 5: Profit
        SplitCalculationRow(
            label = "سود",
            percentValue = state.profitPercent,
            onPercentChange = viewModel::onProfitPercentChange,
            amountValue = state.profitAmount,
            isLocked = locks.lockF,
            onToggleLock = { viewModel.toggleLock("F") },
            onInfoClick = {
                viewModel.showInfo("سود فروشنده", "سود بر مجموع قیمت خام و اجرت محاسبه می‌شود.")
            }
        )

        // Row 6: Tax
        SplitCalculationRow(
            label = "مالیات",
            percentValue = state.taxPercent,
            onPercentChange = viewModel::onTaxPercentChange,
            amountValue = state.taxAmount,
            isLocked = locks.lockH,
            onToggleLock = { viewModel.toggleLock("H") },
            onInfoClick = {
                viewModel.showInfo("مالیات", "مالیات بر ارزش افزوده روی مجموع اجرت و سود محاسبه می‌شود.")
            }
        )

        Divider(
            color = BorderColor,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Row 7: Total Price (Highlighted)
        CalculatorInputRow(
            label = "قیمت کل",
            value = state.totalPrice,
            onValueChange = {},
            unitText = "تومان",
            isReadOnly = true,
            isHighlighted = true
        )

        // Row 8: Total Costs
        CalculatorInputRow(
            label = "مجموع هزینه‌ها",
            value = state.totalCosts,
            onValueChange = {},
            unitText = "تومان",
            isReadOnly = true
        )

        // Row 9: Final Percentage
        CalculatorInputRow(
            label = "درصد نهایی",
            value = state.finalPercent,
            onValueChange = {},
            unitText = "٪",
            isReadOnly = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Receipt Button
            OutlinedButton(
                onClick = viewModel::openReceiptForCurrentForm,
                modifier = Modifier
                    .size(width = 52.dp, height = 48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Receipt,
                    contentDescription = "Receipt",
                    modifier = Modifier.size(20.dp),
                    tint = PrimaryDark
                )
            }

            // Clear Button
            OutlinedButton(
                onClick = { viewModel.onClearForm(keepLocks = true) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CleaningServices,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFEF4444)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "پاک کردن", color = Color(0xFFEF4444), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            // Save Sale Button
            Button(
                onClick = viewModel::onSaveSale,
                modifier = Modifier
                    .weight(1.3f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.editingId != null) Color(0xFFF59E0B) else PrimaryDark,
                    contentColor = White
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (state.editingId != null) "بروزرسانی" else "ثبت فروش",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
