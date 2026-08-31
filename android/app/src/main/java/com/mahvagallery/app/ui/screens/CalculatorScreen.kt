package com.mahvagallery.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mahvagallery.app.R
import com.mahvagallery.app.ui.components.HeaderBar
import com.mahvagallery.app.ui.components.InputRow
import com.mahvagallery.app.ui.components.SplitInputRow
import com.mahvagallery.app.ui.theme.BgLight
import com.mahvagallery.app.ui.theme.BorderColor
import com.mahvagallery.app.ui.theme.PrimaryDark
import com.mahvagallery.app.ui.theme.White
import com.mahvagallery.app.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Force Right-to-Left (Persian)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BgLight
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                // Central App Container (Centered on wider tablet screens, full width on phone)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White)
                ) {
                    // Header Bar
                    HeaderBar()

                    // Scrollable Form Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Row 1: Raw Gold Price (قیمت طلای خام)
                        InputRow(
                            label = stringResource(id = R.string.raw_gold_price_label),
                            value = uiState.rawGoldPrice,
                            onValueChange = viewModel::onRawGoldPriceChange,
                            unitText = stringResource(id = R.string.unit_toman),
                            keyboardType = KeyboardType.Number
                        )

                        // Row 2: Weight (وزن)
                        InputRow(
                            label = stringResource(id = R.string.weight_label),
                            value = uiState.weight,
                            onValueChange = viewModel::onWeightChange,
                            unitText = stringResource(id = R.string.unit_gram),
                            keyboardType = KeyboardType.Decimal
                        )

                        // Row 3: Calculated Raw Price (قیمت خام)
                        InputRow(
                            label = stringResource(id = R.string.raw_price_label),
                            value = uiState.rawPrice,
                            onValueChange = {},
                            unitText = stringResource(id = R.string.unit_toman),
                            isReadOnly = true
                        )

                        // Row 4: Ojrat (اجرت)
                        SplitInputRow(
                            label = stringResource(id = R.string.ojrat_label),
                            percentValue = uiState.ojratPercent,
                            onPercentChange = viewModel::onOjratPercentChange,
                            amountValue = uiState.ojratAmount
                        )

                        // Row 5: Profit (سود)
                        SplitInputRow(
                            label = stringResource(id = R.string.profit_label),
                            percentValue = uiState.profitPercent,
                            onPercentChange = viewModel::onProfitPercentChange,
                            amountValue = uiState.profitAmount
                        )

                        // Row 6: Tax (مالیات)
                        SplitInputRow(
                            label = stringResource(id = R.string.tax_label),
                            percentValue = uiState.taxPercent,
                            onPercentChange = viewModel::onTaxPercentChange,
                            amountValue = uiState.taxAmount
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Row 7: Total Price (قیمت کل)
                        InputRow(
                            label = stringResource(id = R.string.total_price_label),
                            value = uiState.totalPrice,
                            onValueChange = {},
                            unitText = stringResource(id = R.string.unit_toman),
                            isReadOnly = true
                        )

                        // Row 8: Final Percentage (درصد نهایی)
                        InputRow(
                            label = stringResource(id = R.string.final_percentage_label),
                            value = uiState.finalPercent,
                            onValueChange = {},
                            unitText = stringResource(id = R.string.unit_percent_sym),
                            isReadOnly = true
                        )

                        // Bottom spacing to prevent sticky button overlap
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }

                // Sticky Bottom Footer with Recalculate Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(White)
                        .shadow(elevation = 8.dp)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = viewModel::onReset,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryDark,
                            contentColor = White
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(id = R.string.btn_recalculate),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
